package com.honvay.flyai.langchain.document;

import dev.langchain4j.data.document.DocumentSource;
import dev.langchain4j.data.document.Metadata;

import java.io.IOException;
import java.io.InputStream;

public class StreamDocumentSource implements DocumentSource {

    private final InputStream source;

    private final Metadata metadata;

    public StreamDocumentSource(InputStream source,
                                Metadata metadata) {
        this.source = source;
        this.metadata = metadata;
    }

    public StreamDocumentSource(InputStream source) {
        this.source = source;
        this.metadata = new Metadata();
    }


    @Override
    public InputStream inputStream() throws IOException {
        return source;
    }

    @Override
    public Metadata sourceMetadata() {
        return metadata;
    }
}
