package com.honvay.flyai.app.application.impl;

import com.honvay.flyai.app.application.ApplicationQueryService;
import com.honvay.flyai.app.domain.model.Application;
import com.honvay.flyai.app.domain.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationQueryServiceImpl implements ApplicationQueryService {

    private final ApplicationRepository applicationRepository;

    public ApplicationQueryServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Application> find(){
        List<Application> applications = applicationRepository.findAll();
        return applications;
    }
}
