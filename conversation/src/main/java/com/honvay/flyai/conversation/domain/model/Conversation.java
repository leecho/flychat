package com.honvay.flyai.conversation.domain.model;

import com.honvay.flyai.app.domain.model.Application;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class Conversation {

    /**
     * 对话ID
     */
    private Long id;


    private String name;

    private Application application;

    private List<Message> messages;
    /**
     * 用户ID
     */
    private Long userId;

    private Date createTime;

    public void addMessage(Message message){
        if(this.messages == null){
            this.messages = new ArrayList<>();
        }
        this.messages.add(message);
    }

}
