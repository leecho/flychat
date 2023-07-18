package com.honvay.flychat.knowledge.domain.repository;

import com.honvay.flychat.knowledge.domain.model.Application;
import com.honvay.flychat.knowledge.domain.model.ApplicationKnowledge;
import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;

import java.util.List;

public interface ApplicationKnowledgeRepository {

    List<KnowledgeBase> findByApplication(Application application);

}
