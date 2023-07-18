package com.honvay.flychat.conversation.controller;

import com.honvay.flychat.conversation.domain.Conversation;
import lombok.Data;

@Data
public class ConversationVo {

    public Long chatId;

    private String result;

    public static ConversationVo from(Conversation conversation){
        ConversationVo conversationVo = new ConversationVo();
        conversationVo.setResult(conversation.getResult());
        conversationVo.setChatId(conversationVo.getChatId());
        return conversationVo;
    }

}
