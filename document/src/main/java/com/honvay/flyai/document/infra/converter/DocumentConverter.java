package com.honvay.flyai.document.infra.converter;

import com.honvay.flyai.document.domain.model.*;
import com.honvay.flyai.document.infra.po.DocumentSegmentPo;
import com.honvay.flyai.document.infra.po.DocumentPo;
import org.springframework.stereotype.Component;

@Component
public class DocumentConverter {

    public DocumentPo convert(Document document) {
        DocumentPo documentPo = new DocumentPo();
        documentPo.setSplitType(document.getSplitType().getCode());
        //knowledgeItemPo.setSplitStep();
        documentPo.setName(document.getName());
        documentPo.setId(documentPo.getId());

        return documentPo;
    }

    public Document convert(DocumentPo documentPo) {
        Document document = new Document();
        document.setSplitType(SplitType.of(documentPo.getSplitType()));
        document.setName(document.getName());
        document.setId(document.getId());

        return document;
    }


    public DocumentSegmentPo convert(DocumentSegment documentSegment){
        DocumentSegmentPo documentSegmentPo = new DocumentSegmentPo();
        documentSegmentPo.setId(documentSegment.getId());
        documentSegmentPo.setName(documentSegment.getSource());
        documentSegmentPo.setSegment(documentSegment.getSegment());
        documentSegmentPo.setEmbedding(documentSegment.getEmbedding());
        documentSegmentPo.setStatus(documentSegment.getStatus().getCode());
        return documentSegmentPo;
    }

    /**
     * @param documentSegmentPo
     * @return
     */
    public DocumentSegment convert(DocumentSegmentPo documentSegmentPo){
        DocumentSegment documentSegment = new DocumentSegment();
        documentSegment.setId(documentSegmentPo.getId());
        documentSegment.setEmbedding(documentSegmentPo.getEmbedding());
        documentSegment.setSource(documentSegmentPo.getName());
        documentSegment.setSegment(documentSegmentPo.getSegment());
        documentSegment.setStatus(DocumentSegmentStatus.of(documentSegmentPo.getStatus()));
        return documentSegment;
    }

}
