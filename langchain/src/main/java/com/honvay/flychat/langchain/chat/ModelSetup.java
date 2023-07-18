package com.honvay.flychat.langchain.chat;

import lombok.Data;

@Data
public class ModelSetup {

    private String modelName;

    private Double temperature;

    private String apiKey;

}
