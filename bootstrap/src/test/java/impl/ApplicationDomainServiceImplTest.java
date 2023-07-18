package impl;

import com.honvay.flychat.application.FlychatApplication;
import com.honvay.flychat.knowledge.domain.model.*;
import com.honvay.flychat.knowledge.domain.repository.ApplicationRepository;
import com.honvay.flychat.knowledge.domain.service.impl.ApplicationDomainServiceImpl;
import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FlychatApplication.class)
class ApplicationDomainServiceImplTest {

    @Autowired
    private ApplicationDomainServiceImpl applicationDomainService;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Test
    void create() {
        Application application = new Application();
        application.setName("应用1");
        application.setLogo("logo");
        application.setIntroduction("应用介绍");
        Owner owner = new Owner();
        owner.setId(1L);
        application.setOwner(owner);
        ApplicationKnowledge knowledge = new ApplicationKnowledge();
        knowledge.setSimilarity(0.8);
        knowledge.setRelevantSize(1);
        application.setKnowledge(knowledge);
        ApplicationModel model = new ApplicationModel();
        model.setModelName("gpt-turbo-3");
        model.setTemperature(0.8);
        model.setTopk(5);
        application.setModel(model);
        applicationDomainService.create(application);
    }

    @Test
    void update() {

        Application application = new Application();
        application.setId(4L);
        application.setName("应用2");
        application.setLogo("logo");
        application.setIntroduction("应用介绍");
        ApplicationKnowledge knowledge = new ApplicationKnowledge();
        knowledge.setSimilarity(0.9);
        knowledge.setRelevantSize(1);
        application.setKnowledge(knowledge);
        ApplicationModel model = new ApplicationModel();
        model.setModelName("gpt-turbo-5");
        model.setTemperature(0.8);
        model.setTopk(5);
        application.setModel(model);
        applicationDomainService.update(application);

        Application application1 = applicationRepository.get(4L);
        assertEquals(application1.getName(),application.getName());
        assertEquals(application1.getKnowledge().getSimilarity(),application.getKnowledge().getSimilarity());
        assertEquals(application1.getModel().getModelName(),application.getModel().getModelName());

    }

    @Test
    void delete() {

        Application application = new Application();
        application.setId(4L);
        ApplicationKnowledge knowledge = new ApplicationKnowledge();
        application.setKnowledge(knowledge);
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setId(3L);
        application.addKnowledgeBase(knowledgeBase);
        applicationDomainService.addKnowledgeBase(application);
    }
}