package com.honvay.flychat.knowledge.infra.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flychat.knowledge.domain.model.Relevant;
import com.honvay.flychat.knowledge.infra.mapper.KnowledgeDetailMapper;
import com.honvay.flychat.knowledge.infra.po.KnowledgeDetailPo;
import com.honvay.flychat.knowledge.domain.model.KnowledgeDetail;
import com.honvay.flychat.knowledge.infra.factory.KnowledgeConverter;
import com.honvay.flychat.knowledge.domain.model.KnowledgeItemStatus;
import com.honvay.flychat.knowledge.domain.repository.KnowledgeDetailRepository;
import com.pgvector.PGvector;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class KnowledgeDetailRepositoryImpl extends ServiceImpl<KnowledgeDetailMapper, KnowledgeDetailPo> implements KnowledgeDetailRepository {

    private final KnowledgeConverter converter;

    public KnowledgeDetailRepositoryImpl(KnowledgeConverter converter) {
        this.converter = converter;
    }

    @Override
    public List<KnowledgeDetail> findPendingKnowledgeDetails() {
        LambdaQueryWrapper<KnowledgeDetailPo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(KnowledgeDetailPo::getSource, KnowledgeItemStatus.PENDING.getName());
        List<KnowledgeDetailPo> detailPos = this.getBaseMapper().selectList(wrapper);
        return detailPos.stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<KnowledgeDetail> findByKnowledgeItem(List<Long> itemIds) {
        LambdaQueryWrapper<KnowledgeDetailPo> wrapper = Wrappers.lambdaQuery();
        wrapper.in(KnowledgeDetailPo::getItemId, itemIds);
        List<KnowledgeDetailPo> detailPos = this.getBaseMapper().selectList(wrapper);
        return detailPos.stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByItemIds(List<Long> itemIds) {
        LambdaQueryWrapper<KnowledgeDetailPo> wrapper = Wrappers.lambdaQuery();
        wrapper.in(KnowledgeDetailPo::getItemId, itemIds);
        this.getBaseMapper().delete(wrapper);
    }

    @Override
    public List<Relevant> findRelevant(List<Long> knowledgeIds, float[] embedding, Double similarity, int size) {
       return getBaseMapper().findRelevant(knowledgeIds, new PGvector(embedding), similarity, size);
    }
}
