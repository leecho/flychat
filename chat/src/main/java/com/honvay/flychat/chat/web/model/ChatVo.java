package com.honvay.flychat.chat.web.model;

import com.honvay.flychat.chat.domain.model.Chat;
import lombok.Data;

import java.util.Date;

@Data
public class ChatVo {

    private Long id;

    private String name;

    private Date createTime;

    public static ChatVo from(Chat chat){
        ChatVo chatVo = new ChatVo();
        chatVo.setId(chat.getId());
        chatVo.setName(chat.getName());
        chatVo.setCreateTime(chat.getCreateTime());
        return chatVo;
    }

}
