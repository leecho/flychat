package com.honvay.flychat.knowledge.domain.service.impl;

import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.domain.model.Relevant;
import com.honvay.flychat.knowledge.domain.service.KnowledgeBaseDomainService;
import com.honvay.flychat.knowledge.domain.event.KnowledgeItemCreateEvent;
import com.honvay.flychat.knowledge.domain.repository.KnowledgeDetailRepository;
import com.honvay.flychat.knowledge.domain.repository.KnowledgeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class KnowledgeBaseDomainServiceImpl implements KnowledgeBaseDomainService {

    private final ApplicationEventPublisher eventPublisher;

    private final KnowledgeRepository knowledgeRepository;

    private final KnowledgeDetailRepository knowledgeDetailRepository;

    public KnowledgeBaseDomainServiceImpl(ApplicationEventPublisher eventPublisher,
                                          KnowledgeRepository knowledgeRepository,
                                          KnowledgeDetailRepository knowledgeDetailRepository) {
        this.eventPublisher = eventPublisher;
        this.knowledgeRepository = knowledgeRepository;
        this.knowledgeDetailRepository = knowledgeDetailRepository;
    }

    @Override
    public void create(KnowledgeBase knowledgeBase){
        knowledgeRepository.save(knowledgeBase);
    }

    @Override
    public void update(KnowledgeBase knowledgeBase){
        knowledgeRepository.update(knowledgeBase);
    }

    @Override
    public void delete(KnowledgeBase knowledgeBase){

    }

    @Override
    public void addItem(KnowledgeBase knowledgeBase) {
        this.knowledgeRepository.saveItem(knowledgeBase);
        KnowledgeItemCreateEvent event = new KnowledgeItemCreateEvent(knowledgeBase);
        eventPublisher.publishEvent(event);
    }

    @Override
    public void deleteItem(KnowledgeBase knowledgeBase){
        this.knowledgeRepository.deleteItem(knowledgeBase);
    }

    @Override
    public void updateEmbedding(KnowledgeBase knowledgeBase){
        this.knowledgeRepository.updateEmbedding(knowledgeBase);
    }

    @Override
    public List<Relevant> findRelevantDetails(List<Long> knowledgeIds, float[] embedding, Double similarity, int size){
        return this.knowledgeDetailRepository.findRelevant(knowledgeIds,embedding,similarity,size);
    }

}
