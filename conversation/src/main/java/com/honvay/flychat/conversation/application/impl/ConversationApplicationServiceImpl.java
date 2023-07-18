package com.honvay.flychat.conversation.application.impl;

import com.honvay.flychat.chat.application.ChatApplicationService;
import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatMessage;
import com.honvay.flychat.chat.domain.repository.ChatRepository;
import com.honvay.flychat.conversation.application.ConversationApplicationService;
import com.honvay.flychat.conversation.application.ConversationModel;
import com.honvay.flychat.conversation.domain.Conversation;
import com.honvay.flychat.langchain.chat.ChatModelService;
import com.honvay.flychat.langchain.chat.DefaultStreamChatObserver;
import com.honvay.flychat.langchain.chat.ModelSetup;
import com.honvay.flychat.langchain.chat.StreamChatObserver;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
@Service
public class ConversationApplicationServiceImpl implements ConversationApplicationService {

    private final ChatModelService chatModelService;

    private final ChatApplicationService chatApplicationService;

    private final ChatRepository chatRepository;

    private final ConversationModel conversationModel;

    public ConversationApplicationServiceImpl(ChatModelService chatModelService,
                                              ChatApplicationService chatApplicationService,
                                              ChatRepository chatRepository,
                                              @Autowired(required = false) ConversationModel conversationModel) {
        this.chatModelService = chatModelService;
        this.chatApplicationService = chatApplicationService;
        this.chatRepository = chatRepository;
        if(conversationModel == null){
            conversationModel = new ConversationModel();
            conversationModel.setModelName("gpt-3.5-turbo");
            conversationModel.setTemperature(0.8);
            conversationModel.setMaxTokenSize(3069);
        }
        this.conversationModel = conversationModel;
    }


    @Override
    public void converse(Conversation conversation){

        Chat chat = buildChat(conversation);
        List<ChatMessage> messages = buildMessages(conversation, chat);

        ModelSetup modelSetup = conversationModel.toModelSetup();
        String answer = this.chatModelService.chat(this.convert(messages), modelSetup);

        chat.addMessage(ChatMessage.ofAi(answer,null,countTokenSize(answer)));

        conversation.setResult(answer);
    }


    @Override
    public void converse(Conversation conversation,
                         Consumer<String> onResult,
                         Consumer<Void> onComplete,
                         Consumer<Throwable> onError){

        long start = System.currentTimeMillis();
        log.info("对话开始，对话ID：{}",conversation.getId());

        Chat chat = buildChat(conversation);

        List<ChatMessage> messages = buildMessages(conversation, chat);

        log.info("上下文准备好，对话ID：{}，时长：{}",conversation.getId(),(System.currentTimeMillis() - start) / 1000);
        ModelSetup modelSetup = conversationModel.toModelSetup();

        StringBuffer buffer = new StringBuffer();
        Consumer<String> onResultWrapper = s -> {
            buffer.append(s);
            onResult.accept(s);
        };

        Consumer<Void> onCompleteWrapper = s -> {
            String result = buffer.toString();
            ChatMessage aiMessage = ChatMessage.ofAi(result, null, countTokenSize(result));
            chat.addMessage(aiMessage);
            this.chatApplicationService.saveMessages(chat);
            onComplete.accept(s);
            log.info("对话结束，对话ID：{}，时长：{}",conversation.getId(),(System.currentTimeMillis() - start) / 1000);
        };

        StreamChatObserver observer = new DefaultStreamChatObserver(onResultWrapper, onCompleteWrapper, onError);
        this.chatModelService.chat(this.convert(messages),modelSetup,observer );

    }

    @NotNull
    private List<ChatMessage> buildMessages(Conversation conversation, Chat chat) {
        // FIXME: 2023/7/18 计算还有问题
        ChatMessage userMessage = ChatMessage.ofUser(conversation.getPrompt(), null, countTokenSize(conversation.getPrompt()));
        chat.addMessage(userMessage);

        List<ChatMessage> messages = new ArrayList<>();
        messages.add(userMessage);

        //添加历史信息到上下文
        List<ChatMessage> historyMessages = getHistoryMessages(chat, conversation);
        if(historyMessages != null){
            messages.addAll(historyMessages);
        }
        return messages;
    }


    public List<dev.langchain4j.data.message.ChatMessage> convert(List<ChatMessage> messages){
        return messages.stream()
                .map(message -> {
                    if (message.getRole().equals("user")) {
                        return UserMessage.from(message.getContent());
                    } else {
                        return AiMessage.from(message.getContent());
                    }
                })
                .toList();
    }

    private List<ChatMessage> getHistoryMessages(Chat chat,Conversation conversation){
        Integer maxTokens = conversation.getMaxTokens();
        if(maxTokens == null || maxTokens > conversationModel.getMaxTokenSize()){
            maxTokens = conversationModel.getMaxTokenSize();
        }
        if(!conversation.isClean() && conversation.getChatId() != null){
            return getHistoryMessages(chat, maxTokens);
        }else {
            return null;
        }
    }

    private List<ChatMessage> getHistoryMessages(Chat chat, Integer maxTokens){
        int start = 0;
        int size = 5;
        int tokenSize = 0;
        List<ChatMessage> messages = new ArrayList<>();
        while(true){
            List<ChatMessage> message = this.chatRepository.findMessage(chat, start, size);
            //没有消息退出循环
            if(message.size() == 0){
                break;
            }
            for (ChatMessage chatMessage : message) {
                tokenSize += chatMessage.getTokenSize();
                //Token达到长度退出循环
                if(tokenSize > maxTokens){
                    break;
                }
                messages.add(chatMessage);
            }
            //消息数量不足退出循环
            if(message.size() < size){
                break;
            }
            start = (start + 1) * size;
        }

        return messages;
    }


    private int countTokenSize(String content) {
        return chatModelService.estimateTokenCount(content, conversationModel.getModelName());
    }

    private Chat buildChat(Conversation conversation){
        Chat chat = conversation.toChat();
        if(chat.getId() == null){
            chat.create();
            this.chatApplicationService.create(chat);
            conversation.setChatId(chat.getId());
        }
        return chat;
    }


}
