package com.honvay.flychat.knowledge.application;

import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.domain.model.SplitType;

import java.io.File;

public interface KnowledgeApplicationService {

    void create(KnowledgeBase knowledgeBase);

    void update(KnowledgeBase knowledgeBase);

    void addFile(KnowledgeBase knowledgeBase, File file, SplitType splitType);

    void addText(KnowledgeBase knowledgeBase, String name, String text, SplitType splitType);

    void deleteItem(KnowledgeBase knowledgeBase);

}
