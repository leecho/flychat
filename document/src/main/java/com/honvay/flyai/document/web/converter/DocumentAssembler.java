package com.honvay.flyai.document.web.converter;

import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.document.domain.model.Owner;
import com.honvay.flyai.document.web.model.dto.DocumentDto;
import org.springframework.stereotype.Component;

@Component
public class DocumentAssembler {

    public Document assemble(DocumentDto documentDto){
        Document document = new Document();
        document.setId(documentDto.getId());
        document.setName(documentDto.getName());
        Owner owner = new Owner();
        owner.setId(1L);
        document.setOwner(owner);
        return document;
    }

}
