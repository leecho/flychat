package com.honvay.flyai.langchain.chat;

import lombok.Data;

@Data
public class ChatModelSetup {

    private String modelName;

    private Double temperature;

    private String apiKey;

    private Integer maxTokens;

    private Double topP;

    private Double presencePenalty;

    private Double frequencyPenalty;


}
