package com.honvay.flychat.conversation.controller;

import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.web.model.ChatVo;
import com.honvay.flychat.conversation.domain.Conversation;
import lombok.Data;

@Data
public class ConversationVo {

    private String id;

    public ChatVo chat;

    private String message;

    public static ConversationVo from(Conversation conversation){
        ConversationVo conversationVo = new ConversationVo();
        conversationVo.setMessage(conversation.getResult());
        Chat chat = conversation.getChat();
        conversationVo.setChat(ChatVo.from(chat));
        conversationVo.setId(conversation.getId());
        return conversationVo;
    }

}
