package com.honvay.flychat.chat.domain.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Chat {

    private Long id;

    private String name;

    private User user;

    private List<ChatMessage> messages;

    private Date createTime;

    public void addMessage(ChatMessage message){
        if(this.messages == null){
            this.messages = new ArrayList<>();
        }
        this.messages.add(message);
    }

    public boolean isExists(){
        return this.id != null;
    }

    public void create(){
        this.createTime = new Date();
    }

    public Long getApplicationId(){
        return null;
    }

}
