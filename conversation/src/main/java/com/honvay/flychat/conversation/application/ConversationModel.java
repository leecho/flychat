package com.honvay.flychat.conversation.application;

import com.honvay.flychat.langchain.chat.ModelSetup;
import lombok.Data;

@Data
public class ConversationModel {

    private String modelName;

    private Double temperature;

    private Integer maxTokenSize;

    public ModelSetup toModelSetup(){
        ModelSetup modelSetup = new ModelSetup();
        modelSetup.setModelName(this.modelName);
        modelSetup.setTemperature(this.temperature);
        return modelSetup;
    }
}
