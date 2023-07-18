package com.honvay.flychat.chat.infra.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("chat_message_relevant")
public class ChatQuotePo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long messageId;

    private String segment;

    private Double similarity;

    private Long sourceId;

}
