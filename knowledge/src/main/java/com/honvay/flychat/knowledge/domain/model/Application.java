package com.honvay.flychat.knowledge.domain.model;

import lombok.Data;

import java.util.List;


@Data
public class Application {

    private Long id;

    private String name;

    private String logo;

    private String introduction;

    private Owner owner;

    private ApplicationKnowledge knowledge;

    private ApplicationModel model;

    public void addKnowledgeBase(KnowledgeBase knowledgeBase){
        this.knowledge.addKnowledgeBase(knowledgeBase);
    }

    public boolean hasKnowledge(){
        if(this.knowledge == null){
            return false;
        }
        List<KnowledgeBase> knowledgeBases = this.knowledge.getKnowledgeBases();
        return knowledgeBases != null && knowledgeBases.size() > 0;
    }
}
