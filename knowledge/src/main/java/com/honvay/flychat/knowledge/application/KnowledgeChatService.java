package com.honvay.flychat.knowledge.application;

import com.honvay.flychat.knowledge.domain.model.KnowledgeChat;

import java.util.function.Consumer;

public interface KnowledgeChatService {
    String chat(KnowledgeChat knowledgeChat, String question);

    void chat(KnowledgeChat knowledgeChat,
              String question,
              Consumer<String> onResult,
              Consumer<Void> onComplete,
              Consumer<Throwable> onError);
}
