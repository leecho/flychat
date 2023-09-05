package com.honvay.flyai.conversation.application.provider;

import com.honvay.flyai.conversation.domain.model.Conversation;
import com.honvay.flyai.conversation.domain.model.Message;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MessageProvider {

    void setupUserMessage(Conversation conversation, Message message);

    @NotNull
    List<Message> getRelationMessages(Conversation conversation);

    String getName();
}
