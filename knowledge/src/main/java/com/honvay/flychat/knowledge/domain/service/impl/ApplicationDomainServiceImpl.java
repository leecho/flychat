package com.honvay.flychat.knowledge.domain.service.impl;

import com.honvay.flychat.knowledge.domain.model.Application;
import com.honvay.flychat.knowledge.domain.repository.ApplicationRepository;
import com.honvay.flychat.knowledge.domain.service.ApplicationDomainService;
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
        this.applicationRepository.saveKnowledgeBase(application);
    }

    @Override
    public void updateKnowledge(Application application){
        this.applicationRepository.updateKnowledge(application);
    }

    @Override
    public Application get(Long applicationId){
        return  applicationRepository.get(applicationId);
    }

}
