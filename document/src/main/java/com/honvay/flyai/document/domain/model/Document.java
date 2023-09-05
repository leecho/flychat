package com.honvay.flyai.document.domain.model;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Data
public class Document {

    private Long id;

    private  String name;

    private  SplitType splitType;

    private String type;

    private List<DocumentSegment> segments;

    private Owner owner;

    public Document(){

    }

    public static Document of(Long id){
        Document document = new Document();
        document.setId(id);
        return document;
    }

    public Document(File file, SplitType splitType){
        this.name = file.getName();
        this.splitType = splitType;
    }

    public Document(String name, SplitType splitType){
        if(name == null){
            name = "自定义内容";
        }
        this.name = name;
        this.splitType = splitType;
    }

    public void addSegment(DocumentSegment detail){
        if (segments == null) {
            segments = new ArrayList<>();
        }
        segments.add(detail);
    }

}
