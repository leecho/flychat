package com.honvay.flychat.chat.application.impl;

import com.honvay.flychat.chat.application.ChatApplicationService;
import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.service.impl.ChatDomainService;
import org.springframework.stereotype.Service;

@Service
public class ChatApplicationServiceImpl implements ChatApplicationService {

    private final ChatDomainService chatDomainService;

    public ChatApplicationServiceImpl(ChatDomainService chatDomainService) {
        this.chatDomainService = chatDomainService;
    }

    @Override
    public void create(Chat chat){
        this.chatDomainService.create(chat);
    }

    @Override
    public void saveMessages(Chat chat){
        this.chatDomainService.saveMessages(chat);
    }
}
