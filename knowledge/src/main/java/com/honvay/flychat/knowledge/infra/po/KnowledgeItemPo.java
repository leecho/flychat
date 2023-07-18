package com.honvay.flychat.knowledge.infra.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("knowledge_item")
public class KnowledgeItemPo {

    private Long id;

    private Long knowledgeId;

    private Integer splitType;

    private Integer splitStep;

    private String source;

    private Date createTime;

    private Date updateTime;

}
