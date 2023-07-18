package com.honvay.flychat.chat.domain.service.impl;


import com.honvay.flychat.chat.domain.model.Chat;

public interface ChatDomainService {
    void create(Chat chat);

    void saveMessages(Chat chat);
}
