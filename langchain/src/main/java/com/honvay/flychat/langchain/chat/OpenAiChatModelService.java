package com.honvay.flychat.langchain.chat;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.StreamingResultHandler;
import dev.langchain4j.model.chat.StreamingChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.openai.OpenAiStreamingChatModel;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;


@Slf4j
@Component
public class OpenAiChatModelService implements ChatModelService {


    @Override
    public String chat(String promptText, ModelSetup setup) {
        return this.chat(Collections.singletonList(UserMessage.from(promptText)), setup);

    }

    @Override
    public String chat(List<ChatMessage> messages, ModelSetup setup) {

        log.info("Chat on openai，The prompt is: {}", messages);

        OpenAiChatModel chatModel = OpenAiChatModel.builder()
                .apiKey("your-api-key")
                .modelName(setup.getModelName())
                .temperature(setup.getTemperature())
                .logResponses(true)
                .logRequests(true)
                .build();

        AiMessage aiMessage = chatModel.sendMessages(messages);
        return aiMessage.text();

    }


    @Override
    public int estimateTokenCount(String content, String modelName) {
        OpenAiTokenizer openAiTokenizer = new OpenAiTokenizer(modelName);
        return openAiTokenizer.countTokens(content);
    }

    @Override
    public void chat(String promptText, ModelSetup setup, StreamChatObserver observer) {
        this.chat(Collections.singletonList(UserMessage.from(promptText)), setup,observer);
    }

    @Override
    public void chat(List<ChatMessage> messages, ModelSetup setup, StreamChatObserver observer) {

        log.info("Chat on openai，The messages is: {}", messages);
        StreamingChatLanguageModel model = OpenAiStreamingChatModel
                .builder()
                .modelName(setup.getModelName())
                .apiKey("your-api-key")
                .temperature(setup.getTemperature())
                .logResponses(true)
                .logRequests(true)
                .build();

        model.sendMessages(messages, new StreamingResultHandler() {
            @Override
            public void onPartialResult(String partialResult) {
                observer.onResult(partialResult);
            }

            @Override
            public void onComplete() {
                observer.onComplete();
            }

            @Override
            public void onError(Throwable error) {
                observer.onError(error);
            }
        });
    }
}
