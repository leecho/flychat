package com.honvay.flyai.document.infra.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flyai.document.domain.model.DocumentSegment;
import com.honvay.flyai.document.domain.model.DocumentSegmentStatus;
import com.honvay.flyai.document.domain.model.RelevantSegment;
import com.honvay.flyai.document.domain.repository.DocumentSegmentRepository;
import com.honvay.flyai.document.infra.converter.DocumentConverter;
import com.honvay.flyai.document.infra.mapper.DocumentSegmentMapper;
import com.honvay.flyai.document.infra.po.DocumentSegmentPo;
import com.pgvector.PGvector;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;


@Repository
public class DocumentSegmentRepositoryImpl extends ServiceImpl<DocumentSegmentMapper, DocumentSegmentPo> implements DocumentSegmentRepository {

    private final DocumentConverter converter;

    public DocumentSegmentRepositoryImpl(DocumentConverter converter) {
        this.converter = converter;
    }

    @Override
    public List<DocumentSegment> findPendingKnowledgeDetails() {
        LambdaQueryWrapper<DocumentSegmentPo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DocumentSegmentPo::getName, DocumentSegmentStatus.PENDING.getName());
        List<DocumentSegmentPo> detailPos = this.getBaseMapper().selectList(wrapper);
        return detailPos.stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public List<DocumentSegment> findByDocument(List<Long> documentIds) {
        LambdaQueryWrapper<DocumentSegmentPo> wrapper = Wrappers.lambdaQuery();
        wrapper.in(DocumentSegmentPo::getDocumentId, documentIds);
        List<DocumentSegmentPo> detailPos = this.getBaseMapper().selectList(wrapper);
        return detailPos.stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByItemIds(List<Long> itemIds) {
        LambdaQueryWrapper<DocumentSegmentPo> wrapper = Wrappers.lambdaQuery();
        wrapper.in(DocumentSegmentPo::getDocumentId, itemIds);
        this.getBaseMapper().delete(wrapper);
    }

    @Override
    public void deleteByIds(List<Long> ids){
        this.removeBatchByIds(ids);
    }

    @Override
    public List<RelevantSegment> findRelevant(List<Long> knowledgeIds, float[] embedding, Double similarity, int size) {
       return getBaseMapper().findRelevant(knowledgeIds, new PGvector(embedding), similarity, size);
    }
}
