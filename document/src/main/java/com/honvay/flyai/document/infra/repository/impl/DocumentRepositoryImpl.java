package com.honvay.flyai.document.infra.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flyai.document.domain.repository.DocumentRepository;
import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.document.domain.model.DocumentSegment;
import com.honvay.flyai.document.domain.repository.DocumentSegmentRepository;
import com.honvay.flyai.document.infra.converter.DocumentConverter;
import com.honvay.flyai.document.infra.mapper.DocumentMapper;
import com.honvay.flyai.document.infra.po.DocumentPo;
import com.honvay.flyai.document.infra.po.DocumentSegmentPo;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class DocumentRepositoryImpl extends ServiceImpl<DocumentMapper, DocumentPo> implements DocumentRepository {

    private final static Integer BATCH_SIZE = 200;
    private final DocumentConverter converter;
    private final DocumentSegmentRepository documentSegmentRepository;

    public DocumentRepositoryImpl(DocumentConverter converter,
                                  DocumentSegmentRepository documentSegmentRepository) {
        this.converter = converter;
        this.documentSegmentRepository = documentSegmentRepository;
    }

    @Override
    public Document get(Long id) {
        DocumentPo documentPo = this.getBaseMapper().selectById(id);
        return converter.convert(documentPo);
    }

    @Override
    public List<Document> findByUser(Long userId) {
        LambdaQueryWrapper<DocumentPo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(DocumentPo::getUserId, userId);
        List<DocumentPo> documentPos = this.getBaseMapper().selectList(wrapper);
        return documentPos.stream()
                .map(converter::convert)
                .collect(Collectors.toList());
    }

    @Override
    public void save(Document document) {
        DocumentPo documentPo = converter.convert(document);
        this.getBaseMapper().insert(documentPo);
    }

    @Override
    public void update(Document document) {
        DocumentPo documentPo = converter.convert(document);
        this.getBaseMapper().updateById(documentPo);
    }

    @Override
    public void saveSegment(Document document) {
            List<DocumentSegmentPo> detailPos = new ArrayList<>();
            List<DocumentSegment> segments = document.getSegments();
            for (DocumentSegment segment : segments) {
                DocumentSegmentPo documentSegmentPo = converter.convert(segment);
                documentSegmentPo.setDocumentId(document.getId());
                detailPos.add(documentSegmentPo);
            }
            this.documentSegmentRepository.saveBatch(detailPos,BATCH_SIZE);
            int index = 0;
            for (DocumentSegmentPo detailPo : detailPos) {
                DocumentSegment documentSegment = segments.get(index);
                documentSegment.setId(detailPo.getId());
                index++;
        }
    }

    @Override
    public void deleteSegment(Document document) {
        List<Long> itemIds = document.getSegments()
                .stream()
                .map(DocumentSegment::getId)
                .collect(Collectors.toList());
        this.documentSegmentRepository.deleteByIds(itemIds);

    }

    @Override
    public void updateEmbedding(Document document) {
        List<DocumentSegmentPo> segments = document.getSegments()
                .stream()
                .map(converter::convert)
                .collect(Collectors.toList());
        this.documentSegmentRepository.updateBatchById(segments,BATCH_SIZE);
    }

}
