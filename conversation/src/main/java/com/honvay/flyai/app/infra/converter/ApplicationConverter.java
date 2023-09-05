package com.honvay.flyai.app.infra.converter;

import com.honvay.flyai.app.infra.po.ApplicationPo;
import com.honvay.flyai.app.domain.model.Application;
import com.honvay.flyai.app.domain.model.Model;
import com.honvay.flyai.app.domain.model.Reference;
import com.honvay.flyai.document.domain.model.Owner;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConverter {

    public ApplicationPo convert(Application application) {
        ApplicationPo applicationPo = new ApplicationPo();
        assembleInfo(applicationPo, application);
        applicationPo.setOwner(application.getOwner().getId());
        assembleReference(applicationPo, application);
        assembleModel(applicationPo,application);
        return applicationPo;

    }

    private void assembleInfo(ApplicationPo applicationPo, Application application) {
        applicationPo.setId(application.getId());
        applicationPo.setName(application.getName());
        applicationPo.setLogo(application.getLogo());
        applicationPo.setIntroduction(application.getIntroduction());
       this.assembleModel(applicationPo,application);
        this.assembleReference(applicationPo, application);
    }

    private void assembleReference(ApplicationPo applicationPo, Application application) {
        Reference knowledge = application.getReference();
        applicationPo.setSimilarity(knowledge.getSimilarity());
        applicationPo.setRelevantSize(knowledge.getRelevantSize());
    }

    public ApplicationPo convertBase(Application application) {
        ApplicationPo applicationPo = new ApplicationPo();
        assembleInfo(applicationPo, application);
        assembleModel(applicationPo,application);
        assembleReference(applicationPo,application);
        return applicationPo;
    }

    private void assembleModel(ApplicationPo applicationPo,Application application){
        Model model = application.getModel();
        applicationPo.setModelName(model.getModelName());
        // applicationPo.setLimitPrompt(model.getLimitPrompt());
        applicationPo.setTemperature(model.getTemperature());
        applicationPo.setTopP(model.getTopP());
    }

    public ApplicationPo convertReference(Application application) {
        ApplicationPo applicationPo = new ApplicationPo();
        applicationPo.setId(application.getId());
        Reference knowledge = application.getReference();
        applicationPo.setSimilarity(knowledge.getSimilarity());
        applicationPo.setRelevantSize(knowledge.getRelevantSize());
        return applicationPo;
    }

    public Application convert(ApplicationPo applicationPo) {
        Application application = new Application();
        application.setId(applicationPo.getId());
        application.setName(applicationPo.getName());
        application.setLogo(applicationPo.getLogo());
        application.setIntroduction(applicationPo.getIntroduction());

        Owner owner = new Owner();
        owner.setId(applicationPo.getOwner());
        application.setOwner(owner);

        Reference reference = new Reference();
        reference.setSimilarity(applicationPo.getSimilarity());
        reference.setRelevantSize(applicationPo.getRelevantSize());
        application.setReference(reference);

        Model model = new Model();
        model.setModelName(applicationPo.getModelName());
        // model.setLimitPrompt(applicationPo.getLimitPrompt());
        model.setTemperature(applicationPo.getTemperature());
        model.setTopP(applicationPo.getTopP());

        application.setModel(model);

        return application;
    }

}
