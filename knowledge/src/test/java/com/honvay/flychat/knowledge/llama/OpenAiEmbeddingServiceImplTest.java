package com.honvay.flychat.knowledge.llama;

import com.honvay.flychat.knowledge.infra.splitter.SentenceTextSplitter;
import com.honvay.flychat.langchain.llama.embedding.OpenAiEmbeddingService;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSource;
import dev.langchain4j.data.document.FileSystemDocumentLoader;
import dev.langchain4j.data.document.source.FileSystemSource;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


class OpenAiEmbeddingServiceImplTest {

    @Test
    void embed(){
        File file = new File("/Users/user/workspace/ChatGPT/flychat/chat/src/test/resources/story-about-happy-carrot.pdf");

        DocumentSource documentSource = FileSystemSource.from(file);
        Document document = FileSystemDocumentLoader.loadDocument(Paths.get("/Users/liqiu/workspace/ChatGPT/flychat/chat/src/test/resources/story-about-happy-carrot.pdf"));

        SentenceTextSplitter splitter = new SentenceTextSplitter();
        Map<String,Object> properties = new HashMap<>();
        List<String> segments = splitter.split(document, properties);

        OpenAiEmbeddingService openAiService = new OpenAiEmbeddingService(System.getenv("OPENAI_API_KEY"));
        List<float[]> embeddings = openAiService.embed(segments);
        System.out.println(embeddings);
    }

    @Test
    void embedText(){
        String text = "Who is Charlie? Answer in 10 words.";
        OpenAiEmbeddingService openAiService = new OpenAiEmbeddingService(System.getenv("OPENAI_API_KEY"));
        float[] embeddings = openAiService.embed(text);
        System.out.println(Arrays.toString(embeddings));
    }

}