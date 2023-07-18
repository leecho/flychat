package com.honvay.flychat.knowledge.infra.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.infra.mapper.KnowledgeBaseMapper;
import com.honvay.flychat.knowledge.infra.po.KnowledgeDetailPo;
import com.honvay.flychat.knowledge.domain.model.KnowledgeDetail;
import com.honvay.flychat.knowledge.domain.model.KnowledgeItem;
import com.honvay.flychat.knowledge.infra.factory.KnowledgeConverter;
import com.honvay.flychat.knowledge.infra.po.KnowledgeItemPo;
import com.honvay.flychat.knowledge.infra.po.KnowledgeBasePo;
import com.honvay.flychat.knowledge.domain.repository.KnowledgeDetailRepository;
import com.honvay.flychat.knowledge.domain.repository.KnowledgeItemRepository;
import com.honvay.flychat.knowledge.domain.repository.KnowledgeRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class KnowledgeRepositoryImpl extends ServiceImpl<KnowledgeBaseMapper, KnowledgeBasePo> implements KnowledgeRepository {

    private final static Integer BATCH_SIZE = 200;

    private final KnowledgeConverter converter;

    private final KnowledgeItemRepository knowledgeItemRepository;

    private final KnowledgeDetailRepository knowledgeDetailRepository;

    public KnowledgeRepositoryImpl(KnowledgeConverter converter,
                                   KnowledgeItemRepository knowledgeItemRepository,
                                   KnowledgeDetailRepository knowledgeDetailRepository) {
        this.converter = converter;
        this.knowledgeItemRepository = knowledgeItemRepository;
        this.knowledgeDetailRepository = knowledgeDetailRepository;
    }

    @Override
    public KnowledgeBase get(Long id) {
        KnowledgeBasePo knowledgeBasePo = this.getBaseMapper().selectById(id);
        return converter.convert(knowledgeBasePo);
    }

    @Override
    public List<KnowledgeBase> findByUser(Long userId) {
        LambdaQueryWrapper<KnowledgeBasePo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(KnowledgeBasePo::getUserId, userId);
        List<KnowledgeBasePo> knowledgeBasePos = this.getBaseMapper().selectList(wrapper);
        return knowledgeBasePos.stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void save(KnowledgeBase knowledgeBase) {
        KnowledgeBasePo knowledgeBasePo = converter.convert(knowledgeBase);
        this.getBaseMapper().insert(knowledgeBasePo);
    }

    @Override
    public void update(KnowledgeBase knowledgeBase) {
        KnowledgeBasePo knowledgeBasePo = converter.convert(knowledgeBase);
        this.getBaseMapper().updateById(knowledgeBasePo);
    }

    @Override
    public void saveItem(KnowledgeBase knowledgeBase) {
        List<KnowledgeItem> items = knowledgeBase.getItems();
        for (KnowledgeItem item : items) {
            KnowledgeItemPo knowledgeItem = converter.convert(item);
            knowledgeItem.setKnowledgeId(knowledgeBase.getId());
            this.knowledgeItemRepository.save(knowledgeItem);
            item.setId(knowledgeItem.getId());

            List<KnowledgeDetailPo> detailPos = new ArrayList<>();
            List<KnowledgeDetail> details = item.getDetails();
            for (KnowledgeDetail detail : details) {
                KnowledgeDetailPo knowledgeDetail = converter.convert(detail);
                knowledgeDetail.setKnowledgeId(knowledgeBase.getId());
                knowledgeDetail.setItemId(knowledgeItem.getId());
                detailPos.add(knowledgeDetail);
            }
            this.knowledgeDetailRepository.saveBatch(detailPos,BATCH_SIZE);
            int index = 0;
            for (KnowledgeDetailPo detailPo : detailPos) {
                KnowledgeDetail knowledgeDetail = details.get(index);
                knowledgeDetail.setId(detailPo.getId());
                index++;
            }
        }
    }

    @Override
    public void deleteItem(KnowledgeBase knowledgeBase) {
        List<Long> itemIds = knowledgeBase.getItems()
                .stream()
                .map(KnowledgeItem::getId)
                .collect(Collectors.toList());
        this.knowledgeItemRepository.removeBatchByIds(itemIds);
        this.knowledgeDetailRepository.deleteByItemIds(itemIds);

    }

    @Override
    public void updateEmbedding(KnowledgeBase knowledgeBase) {
        List<KnowledgeDetailPo> knowledgeDetails = knowledgeBase.getItems()
                .stream()
                .flatMap(knowledgeItem -> knowledgeItem.getDetails().stream())
                .map(converter::convert)
                .collect(Collectors.toList());
        this.knowledgeDetailRepository.updateBatchById(knowledgeDetails,BATCH_SIZE);
    }


}
