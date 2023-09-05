package com.honvay.flyai.app.domain.model;

import com.honvay.flyai.langchain.chat.ChatModelSetup;
import lombok.Data;

@Data
public class Model {

    private String apiKey;

    private String modelName;

    private Double temperature;

    private Integer maxTokens;

    private Double topP;

    private Double presencePenalty;

    private Double frequencyPenalty;

    public ChatModelSetup toModelSetup(){
        ChatModelSetup chatModelSetup = new ChatModelSetup();
        chatModelSetup.setModelName(this.modelName);
        chatModelSetup.setTemperature(this.temperature);
        chatModelSetup.setApiKey(this.apiKey);
        chatModelSetup.setMaxTokens(this.maxTokens);
        chatModelSetup.setTopP(this.topP);
        chatModelSetup.setPresencePenalty(this.presencePenalty);
        chatModelSetup.setFrequencyPenalty(this.frequencyPenalty);
        return chatModelSetup;
    }

}
