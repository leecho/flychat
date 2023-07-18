package com.honvay.flychat.knowledge.domain.service;

import com.honvay.flychat.knowledge.domain.model.Application;

public interface ApplicationDomainService {
    void create(Application application);

    void update(Application application);

    void delete(Application application);

    void addKnowledgeBase(Application application);

    void updateKnowledge(Application application);

    Application get(Long applicationId);
}
