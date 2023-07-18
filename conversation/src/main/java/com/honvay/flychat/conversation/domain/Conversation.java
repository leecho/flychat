package com.honvay.flychat.conversation.domain;

import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.User;
import lombok.Data;

import java.util.UUID;

@Data
public class Conversation {

    private String id;

    private Long chatId;

    private String prompt;

    private Integer maxTokens;

    private boolean clean;

    private Long userId;

    private String result;

    public void init(){
        this.id = UUID.randomUUID().toString();
    }

    public Chat toChat(){
        Chat chat = new Chat();
        chat.setId(chatId);
        User user = new User();
        user.setId(userId);
        chat.setUser(user);
        return chat;
    }
}
