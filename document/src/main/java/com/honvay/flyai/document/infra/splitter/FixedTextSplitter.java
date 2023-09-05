package com.honvay.flyai.document.infra.splitter;

import com.honvay.flyai.document.domain.model.SplitType;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.document.splitter.CharacterSplitter;
import dev.langchain4j.data.segment.TextSegment;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class FixedTextSplitter implements TextSplitter {

    @Override
    public SplitType getSplitType() {
        return SplitType.FIXED;
    }

    public List<String> split(Document document, Map<String,Object> properties) {
        DocumentSplitter splitter = new CharacterSplitter((int)properties.get("size"),0);
        List<TextSegment> documentSegments = splitter.split(document);
        return documentSegments.stream()
                .map(TextSegment::text)
                .collect(Collectors.toList());
    }
}
