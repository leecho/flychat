package com.honvay.flychat.knowledge.domain.service;

import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.domain.model.Relevant;

import java.util.List;

public interface KnowledgeBaseDomainService {
    void create(KnowledgeBase knowledgeBase);

    void update(KnowledgeBase knowledgeBase);

    void delete(KnowledgeBase knowledgeBase);

    void addItem(KnowledgeBase knowledgeBase);

    void deleteItem(KnowledgeBase knowledgeBase);

    void updateEmbedding(KnowledgeBase knowledgeBase);

    List<Relevant> findRelevantDetails(List<Long> knowledgeIds, float[] embedding, Double similarity, int size);
}
