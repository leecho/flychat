package com.honvay.flyai.conversation.domain.model;

import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Date;
import java.util.List;

@Data
public class Message {

    private Long id;

    private String prompt;

    private String content;

    private String answer;

    private Long userId;

    private List<Quote> quotes;

    private Integer tokenSize;

    private String role;

    private Date createTime;

    public boolean hasQuotes(){
        return CollectionUtils.isNotEmpty(quotes);
    }

    public static Message ofUser(String content, String prompt, List<Quote> quotas, Integer tokenSize) {
        Message message = Message.of( content, prompt, quotas, tokenSize);
        message.setRole("user");
        return message;
    }

    public static Message ofAi(String content, List<Quote> quotes, Integer tokenSize) {
        Message message = Message.of( content, null, quotes, tokenSize);
        message.setRole("ai");
        return message;
    }

    private static Message of(String content, String prompt, List<Quote> quotes, Integer tokenSize) {
        Message message = new Message();
        message.setQuotes(quotes);
        message.setContent(content);
        message.setTokenSize(tokenSize);
        message.setPrompt(prompt);
        return message;
    }

}
