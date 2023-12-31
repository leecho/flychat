package com.honvay.flychat.knowledge.infra.splitter;

import com.honvay.flychat.knowledge.domain.model.SplitType;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.SentenceSplitter;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class SentenceTextSplitter implements TextSplitter {


    @Override
    public SplitType getSplitType() {
        return SplitType.SENTENCE;
    }

    @Override
    public List<String> split(Document document,Map<String,Object> properties) {
        DocumentSplitter splitter = new SentenceSplitter();
        List<TextSegment> documentSegments = splitter.split(document);
        return documentSegments.stream()
                .map(TextSegment::text)
                .collect(Collectors.toList());
    }
}
