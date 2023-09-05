package com.honvay.flyai.app.web;

import com.honvay.flyai.app.domain.repository.ApplicationRepository;
import com.honvay.flyai.framework.web.ResponsePayload;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ResponsePayload
@RestController
@RequestMapping("/application")
public class ApplicationController {

    private final ApplicationRepository applicationRepository;

    private final ApplicationAssembler applicationAssembler;

    public ApplicationController(ApplicationRepository applicationRepository,
                                 ApplicationAssembler applicationAssembler) {
        this.applicationRepository = applicationRepository;
        this.applicationAssembler = applicationAssembler;
    }

    @GetMapping
    public List<ApplicationVo> list(){
        return this.applicationRepository.findAll()
                .stream()
                .map(applicationAssembler::assemble)
                .toList();
    }
}
