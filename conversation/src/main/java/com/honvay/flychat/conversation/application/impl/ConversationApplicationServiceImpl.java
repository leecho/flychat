package com.honvay.flychat.conversation.application.impl;

import com.honvay.flychat.chat.application.ChatApplicationService;
import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatMessage;
import com.honvay.flychat.conversation.application.ConversationApplicationService;
import com.honvay.flychat.conversation.domain.Conversation;
import com.honvay.flychat.conversation.infra.MessageProviderDelegator;
import com.honvay.flychat.langchain.chat.ChatModelService;
import com.honvay.flychat.langchain.chat.DefaultStreamChatObserver;
import com.honvay.flychat.langchain.chat.ChatSetup;
import com.honvay.flychat.langchain.chat.StreamChatObserver;
import dev.langchain4j.data.message.AiMessage;
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

    private final ChatApplicationService chatApplicationService;

    private final MessageProviderDelegator messageProviderDelegator;

    private final String apiKey;

    public ConversationApplicationServiceImpl(ChatModelService chatModelService,
                                              ChatApplicationService chatApplicationService,
                                              MessageProviderDelegator messageProviderDelegator,
                                              @Value("${openai.apiKey:}") String apiKey) {
        this.chatModelService = chatModelService;
        this.chatApplicationService = chatApplicationService;
        this.messageProviderDelegator = messageProviderDelegator;
        this.apiKey = apiKey;
    }


    @Override
    public void converse(Conversation conversation) {

        Chat chat = setupChat(conversation);
        ChatSetup chatSetup = this.buildChatSetup(conversation);

        String answer = this.chatModelService.chat(this.buildMessages(conversation, chat), chatSetup);

        chat.addMessage(ChatMessage.ofAi(conversation.getId(), answer, null, countTokenSize(conversation, answer)));
        conversation.setResult(answer);

        this.chatApplicationService.saveMessages(chat);
    }

    @NotNull
    private ChatSetup buildChatSetup(Conversation conversation) {
        ChatSetup chatSetup = conversation.getModel().toModelSetup();
        chatSetup.setApiKey(apiKey);
        return chatSetup;
    }

    private List<dev.langchain4j.data.message.ChatMessage> buildMessages(Conversation conversation, Chat chat) {

        String prompt = messageProviderDelegator.getPrompt(conversation);
        ChatMessage userMessage = ChatMessage.ofUser(conversation.getId(), prompt, conversation.getPrompt(), countTokenSize(conversation, prompt));
        chat.addMessage(userMessage);

        List<ChatMessage> messages = messageProviderDelegator.getRelation(conversation, chat);
        messages.add(userMessage);

        return this.convert(messages);
    }


    @Override
    public void converse(Conversation conversation,
                         Consumer<String> onResult,
                         Consumer<Void> onComplete,
                         Consumer<Throwable> onError) {

        long start = System.currentTimeMillis();
        log.info("对话开始，对话ID：{}", conversation.getId());

        Chat chat = setupChat(conversation);

        List<dev.langchain4j.data.message.ChatMessage> messages = this.buildMessages(conversation, chat);

        log.info("上下文准备好，对话ID：{}，时长：{}", conversation.getId(), (System.currentTimeMillis() - start) / 1000);
        ChatSetup chatSetup = this.buildChatSetup(conversation);

        StringBuffer buffer = new StringBuffer();
        Consumer<String> onResultWrapper = s -> {
            buffer.append(s);
            onResult.accept(s);
        };

        Consumer<Void> onCompleteWrapper = s -> {
            String result = buffer.toString();
            ChatMessage aiMessage = ChatMessage.ofAi(conversation.getId(), result, conversation.getKnowledge().getQuotes(), countTokenSize(conversation, result));
            chat.addMessage(aiMessage);
            this.chatApplicationService.saveMessages(chat);
            onComplete.accept(s);
            log.info("对话结束，对话ID：{}，时长：{}", conversation.getId(), (System.currentTimeMillis() - start) / 1000);
        };

        StreamChatObserver observer = new DefaultStreamChatObserver(onResultWrapper, onCompleteWrapper, onError);

        this.chatModelService.chat(messages, chatSetup, observer);

    }


    private int countTokenSize(Conversation conversation, String content) {
        return chatModelService.estimateTokenCount(content, conversation.getModel().getModelName());
    }

    private Chat setupChat(Conversation conversation) {
        Chat chat = conversation.toChat();
        if (chat.isExists()) {
            return chat;
        }
        chat.create();
        String prompt = conversation.getPrompt();
        chat.setName(StringUtils.substring(prompt, 0, 20));
        this.chatApplicationService.create(chat);
        conversation.setChat(chat);
        return chat;
    }

    public List<dev.langchain4j.data.message.ChatMessage> convert(List<ChatMessage> messages) {
        return messages.stream()
                .map(message -> message.getRole().equals("user") ? UserMessage.from(message.getContent()) : AiMessage.from(message.getContent()))
                .toList();
    }


}
