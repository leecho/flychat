package com.honvay.flychat.knowledge.domain.model;

import lombok.Data;

@Data
public class ApplicationModel {

    private String modelName;

    private String limitPrompt;

    private Double temperature;

    private Integer topk;

}
