package com.honvay.flychat.conversation.infra;

import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatMessage;
import com.honvay.flychat.chat.domain.model.ChatQuote;
import com.honvay.flychat.conversation.domain.Conversation;
import com.honvay.flychat.conversation.domain.Knowledge;
import com.honvay.flychat.knowledge.application.KnowledgeEmbeddingService;
import com.honvay.flychat.knowledge.domain.model.Relevant;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Component
public class KnowledgeMessageProvider implements MessageProvider{

    private final KnowledgeEmbeddingService knowledgeEmbeddingService;

    public KnowledgeMessageProvider(KnowledgeEmbeddingService knowledgeEmbeddingService) {
        this.knowledgeEmbeddingService = knowledgeEmbeddingService;
    }

    @Override
    public String getPrompt(Conversation conversation) {
        List<Relevant> relevant = this.findRelevant( conversation);
        String promptText = buildPromptText(conversation.getPrompt(), relevant);
        List<ChatQuote> quotes = relevant.stream()
                .map(Relevant::toQuote)
                .toList();
        conversation.addQuote(quotes);
        return promptText;
    }

    @NotNull
    @Override
    public List<ChatMessage> getRelationMessages(Conversation conversation, Chat chat) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "knowledge";
    }


    private String buildPromptText(String question, List<Relevant> relevant) {
        String templateText = this.getPromptTemplateText();
        Map<String, Object> variables = this.getPromptVariables(question, relevant);
        PromptTemplate promptTemplate = PromptTemplate.from(templateText);
        Prompt prompt = promptTemplate.apply(variables);
        return prompt.text();
    }

    private List<Relevant> findRelevant(Conversation conversation) {
        Knowledge knowledge = conversation.getKnowledge();
        return knowledgeEmbeddingService.findRelevant(conversation.getPrompt(), knowledge.getKnowledgeBases(), knowledge.getSimilarity(), knowledge.getRelevantSize());
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

        String information = relevant.stream()
                        .map(Relevant::getSegment)
                        .collect(joining("\n\n"));
        Map<String, Object> variables = new HashMap<>();
        variables.put("question", question);
        variables.put("information", information);
        return variables;
    }
}
