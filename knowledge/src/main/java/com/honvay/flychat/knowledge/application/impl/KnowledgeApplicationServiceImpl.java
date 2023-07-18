package com.honvay.flychat.knowledge.application.impl;

import com.honvay.flychat.knowledge.application.KnowledgeApplicationService;
import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.domain.model.KnowledgeDetail;
import com.honvay.flychat.knowledge.domain.model.KnowledgeItem;
import com.honvay.flychat.knowledge.domain.service.KnowledgeBaseDomainService;
import com.honvay.flychat.knowledge.domain.model.SplitType;
import com.honvay.flychat.knowledge.infra.splitter.KnowledgeSplitter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class KnowledgeApplicationServiceImpl implements KnowledgeApplicationService {

    private final KnowledgeBaseDomainService knowledgeBaseDomainService;

    private final KnowledgeSplitter knowledgeSplitter;

    public KnowledgeApplicationServiceImpl(KnowledgeBaseDomainService knowledgeBaseDomainService,
                                           KnowledgeSplitter knowledgeSplitter) {
        this.knowledgeBaseDomainService = knowledgeBaseDomainService;
        this.knowledgeSplitter = knowledgeSplitter;
    }


    @Override
    public void create(KnowledgeBase knowledgeBase) {
        knowledgeBaseDomainService.create(knowledgeBase);
    }

    @Override
    public void update(KnowledgeBase knowledgeBase) {
        knowledgeBaseDomainService.update(knowledgeBase);
    }


    @Override
    public void addFile(KnowledgeBase knowledgeBase, File file, SplitType splitType) {
        List<String> segments = knowledgeSplitter.split(file, splitType, new HashMap<>());
        log.info("对文件：{}，进行{}分词，分词结果数量：{}",file.getName(),splitType.getName(),segments);
        KnowledgeItem knowledgeItem = new KnowledgeItem(file,splitType);
        knowledgeBase.addItem(knowledgeItem);
        for (String segment : segments) {
            knowledgeItem.addDetail(KnowledgeDetail.create(knowledgeItem.getName(),segment));
        }
        this.knowledgeBaseDomainService.addItem(knowledgeBase);
    }

    @Override
    public void addText(KnowledgeBase knowledgeBase, String name, String text, SplitType splitType) {
        List<String> segments = knowledgeSplitter.split(text, splitType, new HashMap<>());
        KnowledgeItem knowledgeItem = new KnowledgeItem(name,splitType);
        knowledgeBase.addItem(knowledgeItem);
        for (String segment : segments) {
            knowledgeItem.addDetail(KnowledgeDetail.create(knowledgeItem.getName(),segment));
        }
        this.knowledgeBaseDomainService.addItem(knowledgeBase);
    }

    @Override
    public void deleteItem(KnowledgeBase knowledgeBase) {
        this.knowledgeBaseDomainService.deleteItem(knowledgeBase);
    }

}
