package com.honvay.flychat.conversation.infra;

import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatMessage;
import com.honvay.flychat.chat.domain.repository.ChatRepository;
import com.honvay.flychat.conversation.domain.Conversation;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConversationMessageProvider implements MessageProvider {

    private final ChatRepository chatRepository;

    public ConversationMessageProvider(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public String getPrompt(Conversation conversation) {
        return conversation.getPrompt();
    }

    @Override
    @NotNull
    public List<ChatMessage> getRelationMessages(Conversation conversation, Chat chat) {
        // FIXME: 2023/7/18 计算还有问题
        List<ChatMessage> messages = new ArrayList<>();

        //添加历史信息到上下文
        List<ChatMessage> historyMessages = getHistoryMessages(conversation, chat);
        if (historyMessages != null) {
            messages.addAll(historyMessages);
        }
        return messages;
    }

    @Override
    public String getName() {
        return "conversation";
    }


    private List<ChatMessage> getHistoryMessages(Conversation conversation, Chat chat) {
        Integer maxTokens = conversation.getModel().getMaxTokens();
        if (!Boolean.TRUE.equals(conversation.getRelation().isClean()) && chat.isExists()) {
            return getHistoryMessages(chat, maxTokens);
        } else {
            return null;
        }
    }

    private List<ChatMessage> getHistoryMessages(Chat chat, Integer maxTokens) {
        int start = 0;
        int size = 5;
        int tokenSize = 0;
        List<ChatMessage> messages = new ArrayList<>();
        while (true) {
            List<ChatMessage> message = this.chatRepository.findMessage(chat, start, size);
            //没有消息退出循环
            if (message.size() == 0) {
                break;
            }
            for (ChatMessage chatMessage : message) {
                tokenSize += chatMessage.getTokenSize();
                //Token达到长度退出循环
                if (tokenSize > maxTokens) {
                    break;
                }
                messages.add(chatMessage);
            }
            //消息数量不足退出循环
            if (message.size() < size) {
                break;
            }
            start = (start + 1) * size;
        }

        return messages;
    }

}
