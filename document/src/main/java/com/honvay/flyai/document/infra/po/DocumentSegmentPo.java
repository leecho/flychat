package com.honvay.flyai.document.infra.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("document_segment")
public class DocumentSegmentPo {

    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 知识库明细
     */
    private Long documentId;

    /**
     * 源文件名称
     */
    private String name;

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

