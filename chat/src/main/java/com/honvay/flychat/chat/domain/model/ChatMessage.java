package com.honvay.flychat.chat.domain.model;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class ChatMessage {

    private Long id;

    private String content;

    private String prompt;

    private String role;

    private List<ChatQuote> quotes;

    private Integer tokenSize;

    private BigDecimal cost;

    private Date createTime;

    public boolean hasRelevant(){
        return this.quotes != null && this.quotes.size() > 0;
    }

    public static ChatMessage ofUser(String content, String prompt , Integer tokenSize){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("user");
        chatMessage.setContent(content);
        chatMessage.setTokenSize(tokenSize);
        chatMessage.setPrompt(prompt);
        return chatMessage;
    }

    public static ChatMessage ofAi(String content, List<ChatQuote> relevants, Integer tokenSize){
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setRole("ai");
        chatMessage.setQuotes(relevants);
        chatMessage.setContent(content);
        chatMessage.setTokenSize(tokenSize);
        return chatMessage;
    }

}
