package com.honvay.flyai.conversation.controller;

import com.honvay.flyai.conversation.domain.model.Message;
import lombok.Data;

import java.util.Date;

@Data
public class MessageVo {

    private Long id;

    private String content;

    private Long conversationId;

    private Date createTime;

    private String role;

    public static MessageVo from(Message message){
        MessageVo messageVo = new MessageVo();
        messageVo.setId(message.getId());
        messageVo.setContent(message.getContent());
        messageVo.setCreateTime(message.getCreateTime());
        messageVo.setRole(message.getRole());
        return messageVo;
    }

}
