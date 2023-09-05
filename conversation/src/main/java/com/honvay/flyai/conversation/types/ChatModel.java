package com.honvay.flyai.conversation.types;

import lombok.Getter;

public enum ChatModel {
    GPT_35_TURBO("gpt-3.5-turbo",4069),
    GPT_35_TURBO_0613("gpt-3.5-turbo-0613",4069),
    GPT_35_TURBO_16K("gpt-3.5-turbo-16k",16384),
    GPT_35_TURBO_16K_0613("gpt-3.5-turbo-16k-0613",16384),
    GPT_4("gpt-4",8192),
    GPT_4_0613("gpt-4-0613",8192),
    GPT_4_32k("gpt-4-32k",8192),
    GPT_4_32k_0613("gpt-4-32k-0613",8192);

    @Getter
    private final String modelName;

    @Getter
    private final Integer maxTokens;


    ChatModel(String modelName, Integer maxTokens) {
        this.modelName = modelName;
        this.maxTokens = maxTokens;
    }
}
