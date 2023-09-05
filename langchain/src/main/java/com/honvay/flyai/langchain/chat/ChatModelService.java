package com.honvay.flyai.langchain.chat;

import dev.langchain4j.data.message.ChatMessage;

import java.util.List;

public interface ChatModelService {
    String chat(String promptText, ChatModelSetup setup);

    String chat(List<ChatMessage> messages, ChatModelSetup setup);

    int estimateTokenCount(String content, String modelName);

    void chat(String promptText, ChatModelSetup setup, StreamChatObserver observer);

    void chat(List<ChatMessage> messages, ChatModelSetup setup, StreamChatObserver observer);
}
