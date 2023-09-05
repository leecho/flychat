package com.honvay.flyai.document.infra.splitter;


import com.honvay.flyai.document.domain.model.SplitType;
import com.honvay.flyai.document.infra.document.DocumentLoader;
import dev.langchain4j.data.document.*;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class DocumentSplitter {

    private final Map<SplitType, TextSplitter> splitters;

    private final DocumentLoader documentLoader;

    public DocumentSplitter(List<TextSplitter> splitters,
                            DocumentLoader documentLoader) {
        this.documentLoader = documentLoader;

        this.splitters = new HashMap<>();
        for (TextSplitter splitter : splitters) {
            this.splitters.put(splitter.getSplitType(),splitter);
        }
    }

    private TextSplitter route(SplitType splitType){
        TextSplitter textSplitter = splitters.get(splitType);
        if(textSplitter == null){
            throw new UnsupportedOperationException("No splitter for :" + splitType.getName());
        }
        return textSplitter;
    }

    public List<String> split(InputStream source, String documentName, SplitType splitType, Map<String, Object> properties){
        Document document = documentLoader.load(source, documentName);
        return split(document,splitType,properties);
    }

    public List<String> split(File file, SplitType splitType, Map<String, Object> properties){
        Document document = documentLoader.load(file);
        return split(document,splitType,properties);
    }

    private List<String> split(Document document,SplitType splitType, Map<String,Object> properties){
        TextSplitter splitter = this.route(splitType);
        return splitter.split(document,properties);
    }

    public List<String> split(String text,SplitType splitType, Map<String,Object> properties){
        Document document = Document.document(text);
        return split(document,splitType,properties);
    }

}
