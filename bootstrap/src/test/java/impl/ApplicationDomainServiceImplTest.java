package impl;

import com.honvay.flyai.app.domain.model.Application;
import com.honvay.flyai.app.domain.model.Model;
import com.honvay.flyai.app.domain.model.Reference;
import com.honvay.flyai.document.domain.model.*;
import com.honvay.flyai.application.FlyaiApplication;
import com.honvay.flyai.app.domain.repository.ApplicationRepository;
import com.honvay.flyai.app.domain.service.impl.ApplicationDomainServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = FlyaiApplication.class)
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
        Reference knowledge = new Reference();
        knowledge.setSimilarity(0.8);
        knowledge.setRelevantSize(1);
        application.setReference(knowledge);
        Model model = new Model();
        model.setModelName("gpt-turbo-3");
        model.setTemperature(0.8);
        model.setTopP(5.0);
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
        Reference knowledge = new Reference();
        knowledge.setSimilarity(0.9);
        knowledge.setRelevantSize(1);
        application.setReference(knowledge);
        Model model = new Model();
        model.setModelName("gpt-turbo-5");
        model.setTemperature(0.8);
        model.setTopP(5.0);
        application.setModel(model);
        applicationDomainService.update(application);

        Application application1 = applicationRepository.get(4L);
        assertEquals(application1.getName(),application.getName());
        assertEquals(application1.getReference().getSimilarity(),application.getReference().getSimilarity());
        assertEquals(application1.getModel().getModelName(),application.getModel().getModelName());

    }

    @Test
    void delete() {

        Application application = new Application();
        application.setId(4L);
        Reference knowledge = new Reference();
        application.setReference(knowledge);
        Document document = new Document();
        document.setId(3L);
        application.addDocument(document);
        applicationDomainService.addKnowledgeBase(application);
    }
}