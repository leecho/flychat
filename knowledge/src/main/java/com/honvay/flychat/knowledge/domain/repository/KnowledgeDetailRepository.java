package com.honvay.flychat.knowledge.domain.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honvay.flychat.knowledge.domain.model.KnowledgeDetail;
import com.honvay.flychat.knowledge.domain.model.Relevant;
import com.honvay.flychat.knowledge.infra.po.KnowledgeDetailPo;

import java.util.List;

public interface KnowledgeDetailRepository  extends IService<KnowledgeDetailPo> {

    List<KnowledgeDetail> findPendingKnowledgeDetails();

    List<KnowledgeDetail> findByKnowledgeItem(List<Long> itemIds);

    void deleteByItemIds(List<Long> itemIds);

    List<Relevant> findRelevant(List<Long> knowledgeIds, float[] embedding, Double similarity, int size);
}
