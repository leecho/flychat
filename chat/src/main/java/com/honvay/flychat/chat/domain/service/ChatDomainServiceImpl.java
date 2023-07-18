package com.honvay.flychat.chat.domain.service;

import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.repository.ChatRepository;
import com.honvay.flychat.chat.domain.service.impl.ChatDomainService;
import org.springframework.stereotype.Service;

@Service
public class ChatDomainServiceImpl implements ChatDomainService {

    private final ChatRepository chatRepository;

    public ChatDomainServiceImpl(ChatRepository chatRepository) {
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
