package com.honvay.flychat.knowledge.infra.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("knowledge_detail")
public class KnowledgeDetailPo {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 知识库ID
     */
    private Long knowledgeId;

    /**
     * 知识库明细
     */
    private Long itemId;

    /**
     * 源文件名称
     */
    private String source;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 段落
     */
    private String segment;

    /**
     * 词向量
     */
    private float[] embedding;

}

