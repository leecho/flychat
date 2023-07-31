package com.honvay.flychat.langchain.chat;

import dev.langchain4j.data.message.ChatMessage;

import java.util.List;

public interface ChatModelService {
    String chat(String promptText, ChatSetup setup);

    String chat(List<ChatMessage> messages, ChatSetup setup);

    int estimateTokenCount(String content, String modelName);

    void chat(String promptText, ChatSetup setup, StreamChatObserver observer);

    void chat(List<ChatMessage> messages, ChatSetup setup, StreamChatObserver observer);
}
