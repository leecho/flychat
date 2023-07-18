package com.honvay.flychat.knowledge.infra.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("application_knowledge")
public class ApplicationKnowledgePo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long applicationId;

    private Long knowledgeBaseId;
}
