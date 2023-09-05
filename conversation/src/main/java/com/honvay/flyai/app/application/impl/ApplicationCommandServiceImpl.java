package com.honvay.flyai.app.application.impl;

import com.honvay.flyai.app.application.ApplicationCommandService;
import com.honvay.flyai.app.domain.model.Application;
import com.honvay.flyai.app.domain.service.ApplicationDomainService;
import org.springframework.stereotype.Service;

@Service
public class ApplicationCommandServiceImpl implements ApplicationCommandService {

    private final ApplicationDomainService applicationDomainService;

    public ApplicationCommandServiceImpl(ApplicationDomainService applicationDomainService) {
        this.applicationDomainService = applicationDomainService;
    }

    @Override
    public void create(Application application){
        applicationDomainService.create(application);
    }

    @Override
    public void delete(Application application){
        applicationDomainService.delete(application);
    }

}
