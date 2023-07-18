package com.honvay.flychat.knowledge.web.converter;

import com.honvay.flychat.chat.domain.model.User;
import com.honvay.flychat.knowledge.domain.model.Application;
import com.honvay.flychat.knowledge.domain.model.KnowledgeChat;
import com.honvay.flychat.knowledge.web.model.dto.KnowledgeChatDto;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeChatAssembler {

    public KnowledgeChat assemble(KnowledgeChatDto knowledgeChatDto){
        KnowledgeChat knowledgeChat = new KnowledgeChat();
        knowledgeChat.setId(knowledgeChatDto.getChatId());
        Application application = new Application();
        application.setId(knowledgeChatDto.getApplicationId());
        knowledgeChat.setApplication(application);
        User owner = new User();
        owner.setId(knowledgeChatDto.getUserId());
        knowledgeChat.setUser(owner);
        return knowledgeChat;
    }

}
