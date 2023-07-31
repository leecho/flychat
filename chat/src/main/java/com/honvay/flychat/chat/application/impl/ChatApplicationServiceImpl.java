package com.honvay.flychat.chat.application.impl;

import com.honvay.flychat.chat.application.ChatApplicationService;
import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.repository.ChatRepository;
import com.honvay.flychat.chat.domain.service.impl.ChatDomainService;
import org.springframework.stereotype.Service;

@Service
public class ChatApplicationServiceImpl implements ChatApplicationService {

    private final ChatRepository chatRepository;

    public ChatApplicationServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void create(Chat chat){
        this.chatRepository.create(chat);
    }

    @Override
    public void saveMessages(Chat chat){
        this.chatRepository.saveMessages(chat);
    }
}
