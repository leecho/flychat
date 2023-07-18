package com.honvay.flychat.conversation.application;

import com.honvay.flychat.conversation.domain.Conversation;

import java.util.function.Consumer;

public interface ConversationApplicationService {
    void converse(Conversation conversation);

    void converse(Conversation conversation,
                  Consumer<String> onResult,
                  Consumer<Void> onComplete,
                  Consumer<Throwable> onError);
}
