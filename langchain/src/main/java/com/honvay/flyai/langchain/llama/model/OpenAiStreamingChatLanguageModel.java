package com.honvay.flyai.langchain.llama.model;

import dev.ai4j.openai4j.OpenAiClient;
import dev.ai4j.openai4j.chat.ChatCompletionRequest;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.StreamingResultHandler;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.structured.StructuredPromptProcessor;
import dev.langchain4j.model.language.StreamingLanguageModel;
import dev.langchain4j.model.language.TokenCountEstimator;
import dev.langchain4j.model.openai.OpenAiModelName;
import dev.langchain4j.model.openai.OpenAiTokenizer;
import lombok.Builder;

import java.time.Duration;


public class OpenAiStreamingChatLanguageModel implements StreamingLanguageModel, TokenCountEstimator {

    private static final double DEFAULT_TEMPERATURE = 0.7;
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(15);

    private final OpenAiClient client;
    private final String modelName;
    private final Double temperature;
    private final OpenAiTokenizer tokenizer;

    @Builder
    public OpenAiStreamingChatLanguageModel(String apiKey,
                                            String modelName,
                                            Double temperature,
                                            Duration timeout,
                                            Boolean logRequests,
                                            Boolean logResponses) {
        this.client = OpenAiClient.builder()
                .apiKey(apiKey)
                .callTimeout(timeout == null ? DEFAULT_TIMEOUT : timeout)
                .connectTimeout(timeout == null ? DEFAULT_TIMEOUT : timeout)
                .readTimeout(timeout == null ? DEFAULT_TIMEOUT : timeout)
                .writeTimeout(timeout == null ? DEFAULT_TIMEOUT : timeout)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .build();
        this.modelName = modelName == null ? OpenAiModelName.TEXT_DAVINCI_003 : modelName;
        this.temperature = temperature == null ? DEFAULT_TEMPERATURE : temperature;
        this.tokenizer = new OpenAiTokenizer(this.modelName);
    }

    @Override
    public void process(String text, StreamingResultHandler handler) {
        ChatCompletionRequest request = ChatCompletionRequest.builder()
                .model(modelName)
                .stream(true)
                .addUserMessage(text)
                .temperature(temperature)
                .build();

        client.chatCompletion(request)
                .onPartialResponse(partialResponse -> {
                    if (partialResponse.choices() != null) {
                        AiMessage aiMessage = OpenAiConverters.aiMessageFrom(partialResponse);
                        String partialResponseText = aiMessage.text();
                        if (partialResponseText != null) {
                            handler.onPartialResult(partialResponseText);
                        }
                    }
                })
                .onComplete(handler::onComplete)
                .onError(handler::onError)
                .execute();
    }

    @Override
    public void process(Prompt prompt, StreamingResultHandler handler) {
        process(prompt.text(), handler);
    }

    @Override
    public void process(Object structuredPrompt, StreamingResultHandler handler) {
        process(StructuredPromptProcessor.toPrompt(structuredPrompt), handler);
    }

    @Override
    public int estimateTokenCount(String prompt) {
        return tokenizer.countTokens(prompt);
    }

    @Override
    public int estimateTokenCount(Prompt prompt) {
        return estimateTokenCount(prompt.text());
    }

    @Override
    public int estimateTokenCount(Object structuredPrompt) {
        return estimateTokenCount(StructuredPromptProcessor.toPrompt(structuredPrompt));
    }

    @Override
    public int estimateTokenCount(TextSegment textSegment) {
        return 0;
    }

    public static OpenAiStreamingChatLanguageModel withApiKey(String apiKey) {
        return builder().apiKey(apiKey).build();
    }
}