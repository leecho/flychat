package com.honvay.flychat.conversation.infra;

import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatMessage;
import com.honvay.flychat.conversation.domain.Conversation;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface MessageProvider {

    String getPrompt(Conversation conversation);

    @NotNull
    List<ChatMessage> getRelationMessages(Conversation conversation, Chat chat);

    String getName();
}
