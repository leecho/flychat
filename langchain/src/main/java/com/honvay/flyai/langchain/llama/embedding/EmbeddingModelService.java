package com.honvay.flyai.langchain.llama.embedding;

import java.util.List;

public interface EmbeddingModelService {

    List<float[]> embed(List<String> texts);

    float[] embed(String text);
}
