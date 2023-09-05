package com.honvay.flyai.conversation.infra.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("message")
public class MessagePo {

    private Long id;

    private Long conversationId;

    private Long applicationId;

    private String prompt;

    private String content;

    private String role;

    private Integer tokenSize;

    private Long userId;

    private Date createTime;

    private Date updateTime;

}
