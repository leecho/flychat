package com.honvay.flyai.document.application;

import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.document.domain.model.RelevantSegment;

import java.io.File;
import java.util.List;

public interface DocumentApplicationService {
    void create(Document document, File file);

    void create(Document document, String text);

    void deleteSegment(Document document);

    void delete(Document document);

    List<RelevantSegment> findRelevantSegment(String source, List<Long> knowledgeIds, Double similarity, int relevantSize);
}
