package com.honvay.flyai.document.domain.service;

import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.document.domain.model.RelevantSegment;

import java.util.List;

public interface DocumentDomainService {
    void create(Document document);

    void update(Document document);

    void delete(Document document);

    void updateEmbedding(Document document);

    List<RelevantSegment> findByRelevant(List<Long> knowledgeIds, float[] embedding, Double similarity, int size);
}
