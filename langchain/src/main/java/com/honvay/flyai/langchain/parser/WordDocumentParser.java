package com.honvay.flyai.langchain.parser;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentParser;
import dev.langchain4j.data.document.Metadata;
import org.apache.poi.extractor.ExtractorFactory;
import org.apache.poi.extractor.POITextExtractor;

import java.io.IOException;
import java.io.InputStream;

public class WordDocumentParser implements DocumentParser {
    @Override
    public Document parse(InputStream inputStream) {
        POITextExtractor extractor;
        try {
            extractor = ExtractorFactory.createExtractor(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new Document(extractor.getText(),new Metadata());
    }
}
