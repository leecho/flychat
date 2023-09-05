package com.honvay.flyai.document.domain.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honvay.flyai.document.domain.model.DocumentSegment;
import com.honvay.flyai.document.domain.model.RelevantSegment;
import com.honvay.flyai.document.infra.po.DocumentSegmentPo;

import java.util.List;

public interface DocumentSegmentRepository extends IService<DocumentSegmentPo> {

    List<DocumentSegment> findPendingKnowledgeDetails();

    List<DocumentSegment> findByDocument(List<Long> itemIds);

    void deleteByItemIds(List<Long> itemIds);

    void deleteByIds(List<Long> ids);

    List<RelevantSegment> findRelevant(List<Long> knowledgeIds, float[] embedding, Double similarity, int size);
}
