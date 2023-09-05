package com.honvay.flyai.document.domain.service.impl;

import com.honvay.flyai.document.domain.service.DocumentDomainService;
import com.honvay.flyai.document.domain.event.DocumentCreateEvent;
import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.document.domain.model.RelevantSegment;
import com.honvay.flyai.document.domain.repository.DocumentRepository;
import com.honvay.flyai.document.domain.repository.DocumentSegmentRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DocumentDomainServiceImpl implements DocumentDomainService {

    private final ApplicationEventPublisher eventPublisher;

    private final DocumentRepository documentRepository;

    private final DocumentSegmentRepository documentSegmentRepository;

    public DocumentDomainServiceImpl(ApplicationEventPublisher eventPublisher,
                                     DocumentRepository documentRepository,
                                          DocumentSegmentRepository documentSegmentRepository) {
        this.eventPublisher = eventPublisher;
        this.documentRepository = documentRepository;
        this.documentSegmentRepository = documentSegmentRepository;
    }

    @Override
    public void create(Document document){
        documentRepository.save(document);
        DocumentCreateEvent event = new DocumentCreateEvent(document);
        eventPublisher.publishEvent(event);
    }

    @Override
    public void update(Document document){
        this.documentRepository.update(document);
    }

    @Override
    public void delete(Document document){
    }


    @Override
    public void updateEmbedding(Document document){
        this.documentRepository.updateEmbedding(document);
    }

    @Override
    public List<RelevantSegment> findByRelevant(List<Long> knowledgeIds, float[] embedding, Double similarity, int size){
        return this.documentSegmentRepository.findRelevant(knowledgeIds,embedding,similarity,size);
    }
}
