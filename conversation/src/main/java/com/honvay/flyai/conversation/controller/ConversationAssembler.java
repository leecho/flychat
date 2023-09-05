package com.honvay.flyai.conversation.controller;

import com.honvay.flyai.app.domain.model.Application;
import com.honvay.flyai.app.domain.repository.ApplicationRepository;
import com.honvay.flyai.conversation.domain.model.Conversation;
import org.springframework.stereotype.Component;

@Component
public class ConversationAssembler {

    private final ApplicationRepository applicationRepository;

    public ConversationAssembler(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public Conversation assemble(ConversationDto conversationDto){
        Conversation conversation = new Conversation();
        conversation.setUserId(0L);
        Application application = applicationRepository.get(conversationDto.getApplicationId());
        application.setId(conversationDto.getApplicationId());
        conversation.setApplication(application);
        return conversation;
    }

}
