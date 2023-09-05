package com.honvay.flyai.conversation.infra.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("conversation")
public class ConversationPo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long applicationId;

    private String name;

    private Date createTime;

    private Date updateTime;
}
