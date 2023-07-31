package com.honvay.flychat.chat.infra.po;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("chat")
public class ChatPo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private Long applicationId;

    private Long userId;

    private Date createTime;

}
