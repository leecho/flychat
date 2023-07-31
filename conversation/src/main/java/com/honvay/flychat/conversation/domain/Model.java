package com.honvay.flychat.conversation.domain;

import com.honvay.flychat.conversation.types.ChatModel;
import com.honvay.flychat.langchain.chat.ChatSetup;
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

    public void init(){
        if(modelName == null){
            this.modelName = ChatModel.GPT_35_TURBO.getModelName();
            this.maxTokens = ChatModel.GPT_35_TURBO.getMaxTokens();
        }
    }

    public ChatSetup toModelSetup(){
        ChatSetup chatSetup = new ChatSetup();
        chatSetup.setModelName(this.modelName);
        chatSetup.setTemperature(this.temperature);
        chatSetup.setApiKey(this.apiKey);
        chatSetup.setMaxTokens(this.maxTokens);
        chatSetup.setTopP(this.topP);
        chatSetup.setPresencePenalty(this.presencePenalty);
        chatSetup.setFrequencyPenalty(this.frequencyPenalty);
        return chatSetup;
    }

}
