package com.honvay.flyai.document.application.impl;

import com.honvay.flyai.document.application.DocumentApplicationService;
import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.document.domain.model.DocumentSegment;
import com.honvay.flyai.document.domain.model.RelevantSegment;
import com.honvay.flyai.document.domain.service.DocumentDomainService;
import com.honvay.flyai.document.infra.splitter.DocumentSplitter;
import com.honvay.flyai.langchain.llama.embedding.EmbeddingModelService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class DocumentApplicationServiceImpl implements DocumentApplicationService {


    private final DocumentDomainService documentDomainService;

    private final EmbeddingModelService embeddingModelService;

    private final DocumentSplitter documentSplitter;

    public DocumentApplicationServiceImpl(DocumentDomainService documentDomainService,
                                          EmbeddingModelService embeddingModelService,
                                          DocumentSplitter documentSplitter) {
        this.documentDomainService = documentDomainService;
        this.embeddingModelService = embeddingModelService;
        this.documentSplitter = documentSplitter;
    }

    @Override
    public void create(Document document, File file) {
        List<String> segments = documentSplitter.split(file, document.getSplitType(), new HashMap<>());
        log.info("对文件：{}，进行{}分词，分词结果数量：{}", file.getName(), document.getSplitType().getName(), segments);
        this.create(document, segments);
    }

    @Override
    public void create(Document document, String text) {
        List<String> segments = documentSplitter.split(text, document.getSplitType(), new HashMap<>());
        log.info("对文本：{}，进行{}分词，分词结果数量：{}", text, document.getSplitType().getName(), segments);
        this.create(document, segments);
    }

    private void create(Document document, List<String> segments) {
        List<float[]> embeddings = embeddingModelService.embed(segments);
        int index = 0;
        for (String segment : segments) {
            DocumentSegment documentSegment = DocumentSegment.create(document.getName(), segment);
            documentSegment.setEmbedding(embeddings.get(index));
            document.addSegment(documentSegment);
            index++;
        }
        this.documentDomainService.create(document);
    }

    @Override
    public void deleteSegment(Document document) {
        this.documentDomainService.delete(document);
    }

    @Override
    public void delete(Document document) {
        this.documentDomainService.delete(document);
    }

    @Override
    public  List<RelevantSegment> findRelevantSegment(String source, List<Long> knowledgeIds, Double similarity, int relevantSize){
        float[] embed = this.embeddingModelService.embed(source);
        return  this.documentDomainService.findByRelevant(knowledgeIds, embed, similarity, relevantSize);
    }


}
