package com.honvay.flyai.langchain.llama.model;

import dev.ai4j.openai4j.chat.*;
import dev.langchain4j.agent.tool.ToolParameters;
import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.SystemMessage;
import dev.langchain4j.data.message.ToolExecutionResultMessage;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static dev.langchain4j.data.message.AiMessage.aiMessage;

public class OpenAiConverters {

    static List<Message> toOpenAiMessages(List<ChatMessage> messages) {

        return messages.stream()
                .map(OpenAiConverters::toOpenAiMessage)
                .collect(Collectors.toList());
    }

    static Message toOpenAiMessage(ChatMessage message) {

        return Message.builder()
                .role(roleFrom(message))
                .name(nameFrom(message))
                .content(message.text())
                .functionCall(functionCallFrom(message))
                .build();
    }

    private static String nameFrom(ChatMessage message) {
        if (message instanceof ToolExecutionResultMessage) {
            return ((ToolExecutionResultMessage) message).toolName();
        }

        return null;
    }

    private static FunctionCall functionCallFrom(ChatMessage message) {
        if (message instanceof AiMessage) {
            AiMessage aiMessage = (AiMessage) message;
            if (aiMessage.toolExecutionRequest() != null) {
                return FunctionCall.builder()
                        .name(aiMessage.toolExecutionRequest().name())
                        .arguments(aiMessage.toolExecutionRequest().arguments())
                        .build();
            }
        }

        return null;
    }

    static Role roleFrom(ChatMessage message) {
        if (message instanceof AiMessage) {
            return Role.ASSISTANT;
        } else if (message instanceof ToolExecutionResultMessage) {
            return Role.FUNCTION;
        } else if (message instanceof SystemMessage) {
            return Role.SYSTEM;
        } else {
            return Role.USER;
        }
    }

    static List<Function> toFunctions(Collection<ToolSpecification> toolSpecifications) {
        if (toolSpecifications == null) {
            return null;
        }

        return toolSpecifications.stream()
                .map(OpenAiConverters::toFunction)
                .collect(Collectors.toList());
    }

    private static Function toFunction(ToolSpecification toolSpecification) {
        return Function.builder()
                .name(toolSpecification.name())
                .description(toolSpecification.description())
                .parameters(toOpenAiParameters(toolSpecification.parameters()))
                .build();
    }

    private static dev.ai4j.openai4j.chat.Parameters toOpenAiParameters(ToolParameters toolParameters) {
        if (toolParameters == null) {
            return dev.ai4j.openai4j.chat.Parameters.builder().build();
        }
        return dev.ai4j.openai4j.chat.Parameters.builder()
                .properties(toolParameters.properties())
                .required(toolParameters.required())
                .build();
    }

    static AiMessage aiMessageFrom(ChatCompletionResponse response) {
        return AiMessage.aiMessage(response.choices().get(0).delta().content());
    }
}
