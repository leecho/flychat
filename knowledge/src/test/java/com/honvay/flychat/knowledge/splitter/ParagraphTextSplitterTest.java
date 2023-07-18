package com.honvay.flychat.knowledge.splitter;

import com.honvay.flychat.knowledge.infra.splitter.ParagraphTextSplitter;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSource;
import dev.langchain4j.data.document.FileSystemDocumentLoader;
import dev.langchain4j.data.document.source.FileSystemSource;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class ParagraphTextSplitterTest {

    @Test
    void testSplit(){
        File file = new File("/Users/user/workspace/ChatGPT/flychat/chat/src/test/resources/story-about-happy-carrot.pdf");

        DocumentSource documentSource = FileSystemSource.from(file);
        Document document = FileSystemDocumentLoader.loadDocument(Paths.get("/Users/liqiu/workspace/ChatGPT/flychat/chat/src/test/resources/story-about-happy-carrot.pdf"));

        ParagraphTextSplitter splitter = new ParagraphTextSplitter();
        Map<String,Object> properties = new HashMap<>();
        List<String> segments = splitter.split(document, properties);
        for (String segment : segments) {
            System.out.println(segment + " : " + segment.length());
        }
    }

}