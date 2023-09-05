package com.honvay.flyai.app.domain.service.impl;

import com.honvay.flyai.app.domain.service.ApplicationDomainService;
import com.honvay.flyai.app.domain.model.Application;
import com.honvay.flyai.app.domain.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

@Service
public class ApplicationDomainServiceImpl implements ApplicationDomainService {

    private final ApplicationRepository applicationRepository;

    public ApplicationDomainServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public void create(Application application){
        applicationRepository.save(application);
    }

    @Override
    public void update(Application application){
        this.applicationRepository.update(application);
    }

    @Override
    public void delete(Application application){
        this.applicationRepository.delete(application);
    }

    @Override
    public void addKnowledgeBase(Application application){
        this.applicationRepository.saveDocument(application);
    }

    @Override
    public void updateKnowledge(Application application){
        this.applicationRepository.updateReference(application);
    }

    @Override
    public Application get(Long applicationId){
        return  applicationRepository.get(applicationId);
    }

}
