package impl;

import com.honvay.flychat.application.FlychatApplication;
import com.honvay.flychat.knowledge.application.KnowledgeApplicationService;
import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.domain.model.Owner;
import com.honvay.flychat.knowledge.domain.model.SplitType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

@SpringBootTest(classes = FlychatApplication.class)
class KnowledgeBaseApplicationServiceImplTest {

    @Autowired
    private KnowledgeApplicationService knowledgeApplicationService;


    @Test
    void create(){
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setName("测试知识库");
        Owner knowledgeOwner = new Owner();
        knowledgeOwner.setId(1L);
        knowledgeBase.setOwner(knowledgeOwner);
        knowledgeApplicationService.create(knowledgeBase);
    }

    @Test
    void addItem(){
        File file = new File("/Users/user/workspace/ChatGPT/flychat/chat/src/test/resources/story-about-happy-carrot.pdf");
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setId(2L);
        knowledgeApplicationService.addFile(knowledgeBase,file, SplitType.SENTENCE);
    }

}