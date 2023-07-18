package com.honvay.flychat.knowledge.domain.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ApplicationKnowledge {

    private Double similarity;

    private int relevantSize;

    private List<KnowledgeBase> knowledgeBases;

    public void addKnowledgeBase(KnowledgeBase knowledgeBase){
        if(this.knowledgeBases == null){
            this.knowledgeBases = new ArrayList<>();
        }
        this.knowledgeBases.add(knowledgeBase);
    }

}
