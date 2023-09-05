package com.honvay.flyai.conversation.domain.repository;

import com.honvay.flyai.conversation.domain.model.Conversation;
import com.honvay.flyai.conversation.domain.model.Message;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface ConversationRepository {


    @Transactional
    void saveMessages(Conversation conversation);

    List<Message> findMessage(Long conversationId, Integer limit , Integer size);

    void create(Conversation conversation);

    List<Message> findMessage(Conversation conversation, int start, int size);

    List<Conversation> find(Long applicationId);

    List<Message> findMessage(Long conversationId);
}
