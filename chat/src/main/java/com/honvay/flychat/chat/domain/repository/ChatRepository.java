package com.honvay.flychat.chat.domain.repository;


import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatMessage;

import java.util.List;

public interface ChatRepository {

    void create(Chat knowledgeChat);

    List<ChatMessage> findMessage(Chat chat, int start, int size);

    void saveMessages(Chat knowledgeChat);

    List<Chat> find();

    List<ChatMessage> findMessage(Chat chat);
}
