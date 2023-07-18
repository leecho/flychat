package com.honvay.flychat.knowledge.domain.model;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class KnowledgeItem {

    private Long id;

    private  String name;

    private  SplitType splitType;

    private List<KnowledgeDetail> details;

    public KnowledgeItem(){

    }

    public static KnowledgeItem of(Long id){
        KnowledgeItem knowledgeItem = new KnowledgeItem();
        knowledgeItem.setId(id);
        return knowledgeItem;
    }

    public KnowledgeItem(File file,SplitType splitType){
        this.name = file.getName();
        this.splitType = splitType;
    }

    public KnowledgeItem(String name,SplitType splitType){
        if(name == null){
            name = "自定义内容";
        }
        this.name = name;
        this.splitType = splitType;
    }

    public void addDetail(KnowledgeDetail detail){
        if (details == null) {
            details = new ArrayList<>();
        }
        details.add(detail);
    }

}
