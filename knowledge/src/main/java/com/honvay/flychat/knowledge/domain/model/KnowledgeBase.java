package com.honvay.flychat.knowledge.domain.model;


import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class KnowledgeBase {

    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 标签
     */
    private String tags;

    private String logo;

    private Owner owner;

    private List<KnowledgeItem> items;

    public static KnowledgeBase of(Long id){
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setId(id);
        return knowledgeBase;
    }

    public void create(){

    }

    /**
     * @param knowledgeItem
     * @return
     */
    public KnowledgeItem addItem(KnowledgeItem knowledgeItem){
        if(this.items == null){
            this.items = new ArrayList<>();
        }
        this.items.add(knowledgeItem);
        return knowledgeItem;
    }
}
