package com.honvay.flychat.chat.infra.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName("chat_message")
public class ChatMessagePo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String conversationId;

    private Long chatId;

    private String content;

    private String prompt;

    private String role;

    private Integer tokenSize;

    private BigDecimal cost;

    private Date createTime;
}
