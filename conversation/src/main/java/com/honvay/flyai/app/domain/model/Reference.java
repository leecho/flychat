package com.honvay.flyai.app.domain.model;

import com.honvay.flyai.document.domain.model.Document;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Reference {

    private Double similarity;

    private int relevantSize;

    private List<Document> documents;

    public void addDocument(Document document){
        if(this.documents == null){
            this.documents = new ArrayList<>();
        }
        this.documents.add(document);
    }

}
