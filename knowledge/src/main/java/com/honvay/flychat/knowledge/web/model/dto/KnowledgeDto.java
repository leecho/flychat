package com.honvay.flychat.knowledge.web.model.dto;

import lombok.Data;

@Data
public class KnowledgeDto {

    private Long id;

    private String name;

    private String tags;

    private String logo;

    private Integer category;

    private String introduction;

}
