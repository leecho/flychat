package com.honvay.flychat.knowledge.domain.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.infra.po.KnowledgeBasePo;

import java.util.List;

public interface KnowledgeRepository extends IService<KnowledgeBasePo> {

    KnowledgeBase get(Long id);

    List<KnowledgeBase> findByUser(Long userId);

    void save(KnowledgeBase knowledgeBase);

    void update(KnowledgeBase knowledgeBase);

    void saveItem(KnowledgeBase knowledgeBase);

    void deleteItem(KnowledgeBase knowledgeBase);

    void updateEmbedding(KnowledgeBase knowledgeBase);

}
