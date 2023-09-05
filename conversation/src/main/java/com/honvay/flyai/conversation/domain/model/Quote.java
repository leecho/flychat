package com.honvay.flyai.conversation.domain.model;

import lombok.Data;

@Data
public class Quote {

    private Long id;

    private Double similarity;

    private Long documentId;

    private String document;

    private String segment;
}
