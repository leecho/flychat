package com.honvay.flychat.knowledge.infra.factory;

import com.honvay.flychat.knowledge.domain.model.Application;
import com.honvay.flychat.knowledge.domain.model.ApplicationKnowledge;
import com.honvay.flychat.knowledge.domain.model.ApplicationModel;
import com.honvay.flychat.knowledge.domain.model.Owner;
import com.honvay.flychat.knowledge.infra.po.ApplicationPo;
import org.springframework.stereotype.Component;

@Component
public class ApplicationConverter {

    public ApplicationPo convert(Application application) {
        ApplicationPo applicationPo = new ApplicationPo();
        assembleInfo(applicationPo, application);
        applicationPo.setOwner(application.getOwner().getId());
        assembleKnowledge(applicationPo, application);
        assembleModel(applicationPo,application);
        return applicationPo;

    }

    private void assembleInfo(ApplicationPo applicationPo, Application application) {
        applicationPo.setId(application.getId());
        applicationPo.setName(application.getName());
        applicationPo.setLogo(application.getLogo());
        applicationPo.setIntroduction(application.getIntroduction());
       this.assembleModel(applicationPo,application);
        this.assembleKnowledge(applicationPo, application);
    }

    private void assembleKnowledge(ApplicationPo applicationPo, Application application) {
        ApplicationKnowledge knowledge = application.getKnowledge();
        applicationPo.setSimilarity(knowledge.getSimilarity());
        applicationPo.setRelevantSize(knowledge.getRelevantSize());
    }

    public ApplicationPo convertBase(Application application) {
        ApplicationPo applicationPo = new ApplicationPo();
        assembleInfo(applicationPo, application);
        assembleModel(applicationPo,application);
        assembleKnowledge(applicationPo,application);
        return applicationPo;
    }

    private void assembleModel(ApplicationPo applicationPo,Application application){
        ApplicationModel model = application.getModel();
        applicationPo.setModelName(model.getModelName());
        applicationPo.setLimitPrompt(model.getLimitPrompt());
        applicationPo.setTemperature(model.getTemperature());
        applicationPo.setTopk(model.getTopk());
    }

    public ApplicationPo convertKnowledge(Application application) {
        ApplicationPo applicationPo = new ApplicationPo();
        applicationPo.setId(application.getId());
        ApplicationKnowledge knowledge = application.getKnowledge();
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

        ApplicationKnowledge knowledge = new ApplicationKnowledge();
        knowledge.setSimilarity(applicationPo.getSimilarity());
        knowledge.setRelevantSize(applicationPo.getRelevantSize());
        application.setKnowledge(knowledge);

        ApplicationModel model = new ApplicationModel();
        model.setModelName(applicationPo.getModelName());
        model.setLimitPrompt(applicationPo.getLimitPrompt());
        model.setTemperature(applicationPo.getTemperature());
        model.setTopk(applicationPo.getTopk());

        application.setModel(model);

        return application;
    }

}
