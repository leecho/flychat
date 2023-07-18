package com.honvay.flychat.knowledge.web.model.dto;

import lombok.Data;

@Data
public class KnowledgeItemDto {

    public final static int TEXT_TYPE = 1;

    public final static int FILE_TYPE = 2;

    private Long knowledgeId;

    private Long id;

    private String name;

    private Integer splitType;

    private Integer type;

    private String source;

}
