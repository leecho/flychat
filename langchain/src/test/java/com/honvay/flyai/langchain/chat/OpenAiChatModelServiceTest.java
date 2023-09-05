package com.honvay.flyai.langchain.chat;

import dev.langchain4j.model.openai.OpenAiTokenizer;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OpenAiChatModelServiceTest {

    @Test
    void countTokenSize(){
        long start = System.currentTimeMillis();
        OpenAiTokenizer openAiTokenizer = new OpenAiTokenizer("gpt-3.5-turbo");
        openAiTokenizer.countTokens("Hello! How can I assist you today?");
        System.out.println((System.currentTimeMillis() - start) / 1000);
    }

}