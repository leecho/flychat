package com.honvay.flyai.conversation.application.provider;

import com.honvay.flyai.app.domain.model.Relation;
import com.honvay.flyai.conversation.domain.model.Conversation;
import com.honvay.flyai.conversation.domain.model.Message;
import com.honvay.flyai.conversation.domain.repository.ConversationRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ConversationMessageProvider implements MessageProvider {

    private final ConversationRepository conversationRepository;

    public ConversationMessageProvider(ConversationRepository conversationRepository) {
        this.conversationRepository = conversationRepository;
    }

    @Override
    public void setupUserMessage(Conversation conversation,Message message) {
        message.setContent(message.getPrompt());
    }

    @Override
    @NotNull
    public List<Message> getRelationMessages(Conversation conversation) {
        // FIXME: 2023/7/18 计算还有问题
        List<Message> messages = new ArrayList<>();

        //添加历史信息到上下文
        List<Message> historyMessages = getHistoryMessages(conversation);
        if (historyMessages != null) {
            messages.addAll(historyMessages);
        }
        return messages;
    }

    @Override
    public String getName() {
        return "conversation";
    }


    private List<Message> getHistoryMessages(Conversation conversation) {
        Integer maxTokens = conversation.getApplication().getModel().getMaxTokens();
        Relation relation = conversation.getApplication().getRelation();
        if(relation == null){
            return null;
        }
        Integer size = relation.getSize();
        if (size != null && size > 0) {
            return getHistoryMessages(conversation,maxTokens);
        } else {
            return null;
        }
    }

    private List<Message> getHistoryMessages(Conversation conversation,Integer maxTokens) {
        int start = 0;
        int size = 5;
        int tokenSize = 0;
        List<Message> messages = new ArrayList<>();
        while (true) {
            List<Message> message = this.conversationRepository.findMessage(conversation.getId(), start, size);
            //没有消息退出循环
            if (message.size() == 0) {
                break;
            }
            for (Message message1 : message) {
                tokenSize += message1.getTokenSize();
                //Token达到长度退出循环
                if (tokenSize > maxTokens) {
                    break;
                }
                messages.add(message1);
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
