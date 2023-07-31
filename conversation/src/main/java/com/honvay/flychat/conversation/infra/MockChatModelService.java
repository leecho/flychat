package com.honvay.flychat.conversation.infra;

import com.github.javafaker.Faker;
import com.honvay.flychat.langchain.chat.ChatModelService;
import com.honvay.flychat.langchain.chat.ChatSetup;
import com.honvay.flychat.langchain.chat.StreamChatObserver;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@Primary
public class MockChatModelService implements ChatModelService {

    private Faker faker = new Faker();

    @Override
    public String chat(String promptText, ChatSetup setup) {
       return this.chat(Collections.singletonList(UserMessage.from(promptText)),setup);
    }

    @Override
    public String chat(List<ChatMessage> messages, ChatSetup setup) {
        return faker.name().name();
    }

    @Override
    public int estimateTokenCount(String content, String modelName) {
        return 0;
    }

    @Override
    public void chat(String promptText, ChatSetup setup, StreamChatObserver observer) {
        this.chat(Collections.singletonList(UserMessage.from(promptText)),setup,observer);
    }

    @Override
    public void chat(List<ChatMessage> messages, ChatSetup setup, StreamChatObserver observer) {

        int size = 20;
        Thread thread = new Thread(() -> {
            int count = 0;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            while (count < size) {
                try {
                    String name = faker.funnyName().name();
                    observer.onResult(name);
                    Thread.sleep(100);
                } catch (Exception e) {
                    observer.onError(e);
                }
                count++;
            }
            observer.onComplete();
        });
        thread.start();
    }
}
