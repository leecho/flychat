package com.honvay.flychat.knowledge.infra.repository.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flychat.knowledge.infra.mapper.KnowledgeItemMapper;
import com.honvay.flychat.knowledge.infra.po.KnowledgeItemPo;
import com.honvay.flychat.knowledge.domain.repository.KnowledgeItemRepository;
import org.springframework.stereotype.Repository;

@Repository
public class KnowledgeItemRepositoryImpl extends ServiceImpl<KnowledgeItemMapper, KnowledgeItemPo> implements KnowledgeItemRepository {
}
