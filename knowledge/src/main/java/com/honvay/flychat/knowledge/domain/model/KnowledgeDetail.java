package com.honvay.flychat.knowledge.domain.model;

import lombok.Data;

@Data
public class KnowledgeDetail {

    private Long id;

    private float[] embedding;

    private String source;

    private String segment;

    private KnowledgeItemStatus status;

    public KnowledgeDetail(){

    }

    public void embed(float[] embedding){
        this.embedding = embedding;
        this.status = KnowledgeItemStatus.DONE;
    }

    public KnowledgeDetail(String source,String segment,KnowledgeItemStatus status){
        this.source = source;
        this.segment = segment;
        this.status = status;
    }

    public static KnowledgeDetail create(String source, String segment){
        return new KnowledgeDetail(source,segment,KnowledgeItemStatus.PENDING);
    }
}
