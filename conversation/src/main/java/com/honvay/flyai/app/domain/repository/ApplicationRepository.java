package com.honvay.flyai.app.domain.repository;

import com.honvay.flyai.app.domain.model.Application;

import java.util.List;

public interface ApplicationRepository {

    Application get(Long applicationId);

    void save(Application application);

    void update(Application application);

    void delete(Application application);

    void saveDocument(Application application);

    List<Application> findAll();

    void deleteDocument(Application application);

    void updateReference(Application application);

}
