package com.honvay.flychat.chat.application;

import com.honvay.flychat.chat.domain.model.Chat;

public interface ChatApplicationService{

    void create(Chat chat);

    void saveMessages(Chat chat);
}
