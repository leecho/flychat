package com.honvay.flyai.conversation.application.impl;

import com.honvay.flyai.conversation.application.ConversationApplicationService;
import com.honvay.flyai.conversation.domain.model.Conversation;
import com.honvay.flyai.conversation.application.provider.MessageProviderDelegator;
import com.honvay.flyai.conversation.domain.model.Message;
import com.honvay.flyai.conversation.domain.repository.ConversationRepository;
import com.honvay.flyai.langchain.chat.ChatModelService;
import com.honvay.flyai.langchain.chat.DefaultStreamChatObserver;
import com.honvay.flyai.langchain.chat.ChatModelSetup;
import com.honvay.flyai.langchain.chat.StreamChatObserver;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Service
public class ConversationApplicationServiceImpl implements ConversationApplicationService {

    private final ChatModelService chatModelService;

    private final MessageProviderDelegator messageProviderDelegator;

    private final ConversationRepository conversationRepository;

    private final String apiKey;

    public ConversationApplicationServiceImpl(ChatModelService chatModelService,
                                              MessageProviderDelegator messageProviderDelegator,
                                              ConversationRepository conversationRepository,
                                              @Value("${openai.apiKey:}") String apiKey) {
        this.chatModelService = chatModelService;
        this.messageProviderDelegator = messageProviderDelegator;
        this.conversationRepository = conversationRepository;

        this.apiKey = apiKey;
    }


    @Override
    public Message converse(Conversation conversation,String prompt) {
        this.saveIfNecessary(conversation,prompt);
        ChatModelSetup chatModelSetup = this.buildChatSetup(conversation);
        List<ChatMessage> messages = this.buildMessages(conversation, prompt);
        String answer = this.chatModelService.chat(messages, chatModelSetup);
        Message message = Message.ofAi(answer, null, countTokenSize(conversation, answer));
        conversation.addMessage(message);
        this.conversationRepository.saveMessages(conversation);
        return message;
    }

    private void saveIfNecessary(Conversation conversation,String prompt){
        if(conversation.getId() == null){
            String name = StringUtils.substring(prompt, 0, 20);
            conversation.setName(name);
            conversationRepository.create(conversation);
        }
    }

    @NotNull
    private ChatModelSetup buildChatSetup(Conversation conversation) {
        ChatModelSetup chatModelSetup = conversation.getApplication().getModel().toModelSetup();
        chatModelSetup.setApiKey(apiKey);
        return chatModelSetup;
    }

    private List<dev.langchain4j.data.message.ChatMessage> buildMessages(Conversation conversation,String prompt) {

        Message userMessage = Message.ofUser(null,prompt,null,null);
        messageProviderDelegator.setupUserMessage(conversation,userMessage);
        userMessage.setTokenSize(this.countTokenSize(conversation,userMessage.getContent()));
        conversation.addMessage(userMessage);

        List<Message> messages = messageProviderDelegator.getRelationMessages(conversation);
        messages.add(userMessage);

        return this.convert(messages);
    }


    @Override
    public void converse(Conversation conversation,
                         String prompt,
                         Consumer<String> onResult,
                         Consumer<Void> onComplete,
                         Consumer<Throwable> onError) {

        this.saveIfNecessary(conversation,prompt);

        long start = System.currentTimeMillis();
        log.info("对话开始，对话ID：{}", conversation.getId());

        List<dev.langchain4j.data.message.ChatMessage> messages = this.buildMessages(conversation,prompt);

        log.info("上下文准备好，对话ID：{}，时长：{}", conversation.getId(), (System.currentTimeMillis() - start) / 1000);
        ChatModelSetup chatModelSetup = this.buildChatSetup(conversation);

        StringBuffer buffer = new StringBuffer();
        Consumer<String> onResultWrapper = s -> {
            buffer.append(s);
            onResult.accept(s);
        };

        Consumer<Void> onCompleteWrapper = s -> {
            String result = buffer.toString();
            Message aiMessage = Message.ofAi( result, null, countTokenSize(conversation, result));
            conversation.addMessage(aiMessage);
            this.conversationRepository.saveMessages(conversation);
            onComplete.accept(s);
            log.info("对话结束，对话ID：{}，时长：{}", conversation.getId(), (System.currentTimeMillis() - start) / 1000);
        };

        StreamChatObserver observer = new DefaultStreamChatObserver(onResultWrapper, onCompleteWrapper, onError);
        this.chatModelService.chat(messages, chatModelSetup, observer);
    }


    private int countTokenSize(Conversation conversation, String content) {
        return chatModelService.estimateTokenCount(content, conversation.getApplication().getModel().getModelName());
    }

    public List<dev.langchain4j.data.message.ChatMessage> convert(List<Message> messages) {
        return messages.stream()
                .map(message -> message.getRole().equals("user") ? UserMessage.from(message.getContent()) : AiMessage.from(message.getContent()))
                .toList();
    }


}
