package com.honvay.flychat.conversation.application;

import com.honvay.flychat.langchain.chat.ChatSetup;
import lombok.Data;

@Data
public class ConversationModel {

    private String modelName;

    private Double temperature;

    private Integer maxTokenSize;

    public ChatSetup toModelSetup(){
        ChatSetup chatSetup = new ChatSetup();
        chatSetup.setModelName(this.modelName);
        chatSetup.setTemperature(this.temperature);
        return chatSetup;
    }
}
