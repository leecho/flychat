package com.honvay.flychat.langchain.chat;

import lombok.Data;

@Data
public class ChatSetup {

    private String modelName;

    private Double temperature;

    private String apiKey;

    private Integer maxTokens;

    private Double topP;

    private Double presencePenalty;

    private Double frequencyPenalty;


}
