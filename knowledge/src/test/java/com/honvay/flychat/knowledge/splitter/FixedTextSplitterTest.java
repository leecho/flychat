package com.honvay.flychat.knowledge.splitter;

import com.honvay.flychat.knowledge.infra.splitter.FixedTextSplitter;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSource;
import dev.langchain4j.data.document.FileSystemDocumentLoader;
import dev.langchain4j.data.document.source.FileSystemSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class FixedTextSplitterTest {

    @Test
    void split() {
        File file = new File("/Users/user/workspace/ChatGPT/flychat/chat/src/test/resources/story-about-happy-carrot.pdf");

        DocumentSource documentSource = FileSystemSource.from(file);
        Document document = FileSystemDocumentLoader.loadDocument(Paths.get("/Users/liqiu/workspace/ChatGPT/flychat/chat/src/test/resources/story-about-happy-carrot.pdf"));

        FixedTextSplitter splitter = new FixedTextSplitter();
        Map<String,Object> properties = new HashMap<>();
        properties.put("size",200);
        List<String> segments = splitter.split(document, properties);
        for (String segment : segments) {
            Assertions.assertTrue(segment.length() <= 200);
            System.out.println(segment + " : " + segment.length());
        }
    }

    @Test
    void testSplit() {
    }
}