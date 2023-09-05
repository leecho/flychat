package com.honvay.flyai.conversation.application;

import com.honvay.flyai.conversation.domain.model.Conversation;
import com.honvay.flyai.conversation.domain.model.Message;

import java.util.function.Consumer;

public interface ConversationApplicationService {
    Message converse(Conversation conversation,
                     String prompt);

    void converse(Conversation conversation,
                  String prompt,
                  Consumer<String> onResult,
                  Consumer<Void> onComplete,
                  Consumer<Throwable> onError);
}
