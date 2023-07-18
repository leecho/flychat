package com.honvay.flychat.knowledge.application.impl;

import com.honvay.flychat.chat.domain.model.ChatMessage;
import com.honvay.flychat.chat.domain.model.ChatQuote;
import com.honvay.flychat.knowledge.application.KnowledgeChatService;
import com.honvay.flychat.knowledge.application.KnowledgeEmbeddingService;
import com.honvay.flychat.knowledge.domain.model.*;
import com.honvay.flychat.knowledge.domain.service.ApplicationDomainService;
import com.honvay.flychat.chat.domain.service.impl.ChatDomainService;
import com.honvay.flychat.langchain.chat.ChatModelService;
import com.honvay.flychat.langchain.chat.DefaultStreamChatObserver;
import com.honvay.flychat.langchain.chat.ModelSetup;
import com.honvay.flychat.langchain.chat.StreamChatObserver;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static java.util.stream.Collectors.joining;

@Service
public class KnowledgeChatServiceImpl implements KnowledgeChatService {

    private final ApplicationDomainService applicationDomainService;

    private final KnowledgeEmbeddingService knowledgeEmbeddingService;

    private final ChatDomainService chatDomainService;

    private final ChatModelService chatModelService;

    public KnowledgeChatServiceImpl(ApplicationDomainService applicationDomainService,
                                    KnowledgeEmbeddingService knowledgeEmbeddingService,
                                    ChatDomainService chatDomainService,
                                    ChatModelService chatModelService) {
        this.applicationDomainService = applicationDomainService;
        this.knowledgeEmbeddingService = knowledgeEmbeddingService;
        this.chatDomainService = chatDomainService;
        this.chatModelService = chatModelService;
    }


    @Override
    public String chat(KnowledgeChat knowledgeChat, String question) {

        saveIfNew(knowledgeChat);

        Application application = this.applicationDomainService.get(knowledgeChat.getApplication().getId());
        knowledgeChat.setApplication(application);

        List<Relevant> relevant = this.findRelevant(question, knowledgeChat);
        String promptText = buildPromptText(question, relevant);

        List<ChatQuote> quotes = relevant.stream()
                .map(Relevant::toQuote)
                .toList();

        knowledgeChat.addMessage(ChatMessage.ofUser(question, promptText, countTokenSize(promptText, application.getModel())));

        ModelSetup modelSetup = assembleModelSetup(application);
        String answer = this.chatModelService.chat(promptText, modelSetup);

        knowledgeChat.addMessage(ChatMessage.ofAi(answer, quotes, countTokenSize(answer, application.getModel())));

        // TODO: 2023/7/17 计费
        this.chatDomainService.saveMessages(knowledgeChat);

        return answer;
    }

    private ModelSetup assembleModelSetup(Application application) {
        ModelSetup modelSetup = new ModelSetup();
        ApplicationModel model = application.getModel();
        modelSetup.setModelName(model.getModelName());
        modelSetup.setTemperature(model.getTemperature());
        return modelSetup;
    }

    private void saveIfNew(KnowledgeChat knowledgeChat) {
        if (knowledgeChat.getId() == null) {
            knowledgeChat.create();
            this.chatDomainService.create(knowledgeChat);
        }
    }

    private String buildPromptText(String question, List<Relevant> relevant) {
        String templateText = this.getPromptTemplateText();
        Map<String, Object> variables = this.getPromptVariables(question, relevant);
        PromptTemplate promptTemplate = PromptTemplate.from(templateText);
        Prompt prompt = promptTemplate.apply(variables);
        return prompt.text();
    }

    private List<Relevant> findRelevant(String question, KnowledgeChat knowledgeChat) {
        ApplicationKnowledge knowledge = knowledgeChat.getApplication().getKnowledge();
        List<Long> knowledgeBaseIds = knowledge.getKnowledgeBases().stream()
                .map(KnowledgeBase::getId).toList();
        return knowledgeEmbeddingService.findRelevant(question, knowledgeBaseIds, knowledge.getSimilarity(), knowledge.getRelevantSize());
    }

    private String getPromptTemplateText() {
        return "Answer the following question to the best of your ability:\n"
                + "\n"
                + "Question:\n"
                + "{{question}}\n"
                + "\n"
                + "Base your answer on the following information:\n"
                + "{{information}}";
    }

    private Map<String, Object> getPromptVariables(String question, List<Relevant> relevant) {

        String information =
                relevant.stream()
                        .map(Relevant::getSegment)
                        .collect(joining("\n\n"));
        Map<String, Object> variables = new HashMap<>();
        variables.put("question", question);
        variables.put("information", information);
        return variables;
    }

    private int countTokenSize(String content, ApplicationModel model) {
        return chatModelService.estimateTokenCount(content, model.getModelName());
    }

    @Override
    public void chat(KnowledgeChat knowledgeChat,
                     String question,
                     Consumer<String> onResult,
                     Consumer<Void> onComplete,
                     Consumer<Throwable> onError) {

        //保存对话
        saveIfNew(knowledgeChat);

        Application application = applicationDomainService.get(knowledgeChat.getApplication().getId());
        knowledgeChat.setApplication(application);

        List<Relevant> relevant = this.findRelevant(question, knowledgeChat);
        String promptText = buildPromptText(question, relevant);

        List<ChatQuote> quotes = relevant.stream()
                .map(Relevant::toQuote)
                .toList();

        ChatMessage userMessage = ChatMessage.ofUser(question, promptText, countTokenSize(promptText, application.getModel()));
        knowledgeChat.addMessage(userMessage);

        StringBuffer buffer = new StringBuffer();

        Consumer<String> onResultWrapper = s -> {
            buffer.append(s);
            onResult.accept(s);
        };

        Consumer<Void> onCompleteWrapper = s -> {
            String result = buffer.toString();
            ChatMessage aiMessage = ChatMessage.ofAi(result, quotes, countTokenSize(result, application.getModel()));
            knowledgeChat.addMessage(aiMessage);
            this.chatDomainService.saveMessages(knowledgeChat);
            onComplete.accept(s);
        };


        ModelSetup modelSetup = assembleModelSetup(application);
        StreamChatObserver observer = new DefaultStreamChatObserver(onResultWrapper, onCompleteWrapper, onError);
        chatModelService.chat(promptText, modelSetup, observer);
    }

}
