package com.honvay.flychat.langchain.chat;

import dev.langchain4j.data.message.ChatMessage;

import java.util.List;

public interface ChatModelService {
    String chat(String promptText, ModelSetup setup);

    String chat(List<ChatMessage> messages, ModelSetup setup);

    int estimateTokenCount(String content, String modelName);

    void chat(String promptText, ModelSetup setup, StreamChatObserver observer);

    void chat(List<ChatMessage> messages, ModelSetup setup, StreamChatObserver observer);
}
