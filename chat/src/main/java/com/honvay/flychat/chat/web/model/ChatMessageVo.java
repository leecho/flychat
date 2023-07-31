package com.honvay.flychat.chat.web.model;

import com.honvay.flychat.chat.domain.model.ChatMessage;
import lombok.Data;

@Data
public class ChatMessageVo {

    private Long id;

    private String content;

    private Long createTime;

    private String role;

    public static ChatMessageVo from(ChatMessage chatMessage){
        ChatMessageVo chatMessageVo = new ChatMessageVo();
        chatMessageVo.setId(chatMessage.getId());
        chatMessageVo.setContent(chatMessage.getContent());
        chatMessageVo.setCreateTime(chatMessage.getCreateTime().getTime());
        chatMessageVo.setRole(chatMessage.getRole());
        return chatMessageVo;
    }

}
