package com.honvay.flyai.conversation.controller;

import com.honvay.flyai.conversation.domain.model.Conversation;
import lombok.Data;

import java.util.Date;

@Data
public class ConversationVo {

    private Long id;

    private String name;

    private Date createTime;

    public static ConversationVo from(Conversation conversation) {
        ConversationVo conversationVo = new ConversationVo();
        conversationVo.setId(conversation.getId());
        conversationVo.setName(conversation.getName());
        conversationVo.setCreateTime(conversation.getCreateTime());
        return conversationVo;
    }


}
