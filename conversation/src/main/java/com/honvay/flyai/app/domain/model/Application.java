package com.honvay.flyai.app.domain.model;

import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.document.domain.model.Owner;
import lombok.Data;

import java.util.List;


@Data
public class Application {

    private Long id;

    private String name;

    private String logo;

    private String introduction;

    private Owner owner;

    private Reference reference;

    private Relation relation;

    private Model model;

    public void addDocument(Document document){
        this.reference.addDocument(document);
    }

    public boolean hasReference(){
        if(this.reference == null){
            return false;
        }
        List<Document> documents = this.reference.getDocuments();
        return documents != null && documents.size() > 0;
    }
}
