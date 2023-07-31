package com.honvay.flychat.chat.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ChatMessage {

    private Long id;

    private String conversationId;

    private String content;

    private String prompt;

    private String role;

    private List<ChatQuote> quotes;

    private Integer tokenSize;

    private BigDecimal cost;

    private Date createTime;

    public boolean hasQuotes() {
        return this.quotes != null && this.quotes.size() > 0;
    }

    public static ChatMessage ofUser(String conversationId, String content, String prompt, Integer tokenSize) {
        ChatMessage chatMessage = ChatMessage.of(conversationId, content, prompt, null, tokenSize);
        chatMessage.setRole("user");
        return chatMessage;
    }

    public static ChatMessage ofAi(String conversationId, String content, List<ChatQuote> quotes, Integer tokenSize) {
        ChatMessage chatMessage = ChatMessage.of(conversationId, content, null, quotes, tokenSize);
        chatMessage.setRole("ai");
        return chatMessage;
    }

    private static ChatMessage of(String conversationId, String content, String prompt, List<ChatQuote> quotes, Integer tokenSize) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setConversationId(conversationId);
        chatMessage.setQuotes(quotes);
        chatMessage.setContent(content);
        chatMessage.setTokenSize(tokenSize);
        chatMessage.setPrompt(prompt);
        return chatMessage;
    }
}
