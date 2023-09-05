package com.honvay.flyai.document.domain.model;

import lombok.Data;

@Data
public class DocumentSegment {

    private Long id;

    private float[] embedding;

    private String source;

    private String segment;

    private DocumentSegmentStatus status;

    public DocumentSegment() {

    }

    public void embed(float[] embedding) {
        this.embedding = embedding;
        this.status = DocumentSegmentStatus.DONE;
    }

    public DocumentSegment(String source, String segment, DocumentSegmentStatus status) {
        this.source = source;
        this.segment = segment;
        this.status = status;
    }

    public static DocumentSegment create(String source, String segment) {
        return new DocumentSegment(source, segment, DocumentSegmentStatus.PENDING);
    }
}
