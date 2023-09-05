package com.honvay.flyai.conversation.application.provider;

import com.honvay.flyai.conversation.domain.model.Conversation;
import com.honvay.flyai.conversation.domain.model.Message;
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

    public void setupUserMessage(Conversation conversation,Message message){
        this.getMessageProvider(conversation).setupUserMessage(conversation,message);
    }

    public List<Message> getRelationMessages(Conversation conversation){
        return this.getMessageProvider(conversation).getRelationMessages(conversation);
    }

    private MessageProvider getMessageProvider(Conversation conversation) {
        return conversation.getApplication().hasReference() ? providers.get("knowledge") : providers.get("conversation");
    }

}
