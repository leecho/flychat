package com.honvay.flyai.document.domain.model;

import lombok.Data;

@Data
public class RelevantSegment {

    private Long segmentId;

    private String segment;

    private Double similarity;

    private String source;

    private Long documentId;

    private String documentName;



}
