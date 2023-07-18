package com.honvay.flychat.knowledge.application;

import com.honvay.flychat.knowledge.domain.model.Relevant;

import java.util.List;

public interface KnowledgeEmbeddingService {
    List<Relevant> findRelevant(String source, List<Long> knowledgeIds, Double similarity, int relevantSize);
}
