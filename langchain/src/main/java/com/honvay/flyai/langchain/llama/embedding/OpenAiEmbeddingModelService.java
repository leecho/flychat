package com.honvay.flyai.langchain.llama.embedding;

import dev.langchain4j.data.embedding.Embedding;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.openai.OpenAiEmbeddingModel;
import dev.langchain4j.model.openai.OpenAiModelName;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OpenAiEmbeddingModelService implements EmbeddingModelService {

    private final String apiKey;

    public OpenAiEmbeddingModelService(@Value("${openai.apiKey:}") String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
   public List<float[]> embed(List<String> texts){

       List<TextSegment> segments = texts.stream()
               .map(TextSegment::from)
               .collect(Collectors.toList());

       EmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
               .apiKey(apiKey) // https://platform.openai.com/account/api-keys
               .modelName(OpenAiModelName.TEXT_EMBEDDING_ADA_002)
               .timeout(Duration.ofSeconds(15))
               .build();

        List<Embedding> embeddings = embeddingModel.embedAll(segments);
       return embeddings
               .stream()
               .map(Embedding::vector)
               .collect(Collectors.toList());
   }

    @Override
    public float[] embed(String text) {

        EmbeddingModel embeddingModel = OpenAiEmbeddingModel.builder()
                .apiKey(apiKey) // https://platform.openai.com/account/api-keys
                .modelName(OpenAiModelName.TEXT_EMBEDDING_ADA_002)
                .timeout(Duration.ofSeconds(15))
                .build();

        Embedding embedding = embeddingModel.embed(text);
        return embedding.vector();
    }

}
