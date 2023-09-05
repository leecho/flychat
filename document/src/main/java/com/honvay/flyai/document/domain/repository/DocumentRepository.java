package com.honvay.flyai.document.domain.repository;

import com.baomidou.mybatisplus.extension.service.IService;
import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.document.infra.po.DocumentPo;

import java.util.List;

public interface DocumentRepository extends IService<DocumentPo> {
    Document get(Long id);

    List<Document> findByUser(Long userId);

    void save(Document document);

    void update(Document document);

    void saveSegment(Document document);

    void deleteSegment(Document document);

    void updateEmbedding(Document document);
}
