package com.honvay.flychat.conversation.infra;

import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatMessage;
import com.honvay.flychat.conversation.domain.Conversation;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MessageProviderDelegator {

    private final Map<String,MessageProvider> providers;

    public MessageProviderDelegator(List<MessageProvider> providers) {
        this.providers = providers.stream()
                .collect(Collectors.toMap(MessageProvider::getName,
                        provider -> provider));
    }

    public String getPrompt(Conversation conversation){
        return this.getMessageProvider(conversation).getPrompt(conversation);
    }

    public List<ChatMessage> getRelation(Conversation conversation, Chat chat){
        return this.getMessageProvider(conversation).getRelationMessages(conversation,chat);
    }

    private MessageProvider getMessageProvider(Conversation conversation) {
        return CollectionUtils.isNotEmpty(conversation.getKnowledge().getKnowledgeBases()) ? providers.get("knowledge") : providers.get("conversation");
    }

}
