package com.honvay.flychat.knowledge.domain.repository;

import com.honvay.flychat.knowledge.domain.model.Application;

public interface ApplicationRepository {

    Application get(Long applicationId);

    void save(Application application);

    void update(Application application);

    void delete(Application application);

    void saveKnowledgeBase(Application application);

    void deleteKnowledgeBase(Application application);

    void updateKnowledge(Application application);

}
