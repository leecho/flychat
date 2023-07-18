package com.honvay.flychat.knowledge.infra.document;

import com.honvay.flychat.langchain.document.StreamDocumentSource;
import com.honvay.flychat.langchain.parser.WordDocumentParser;
import dev.langchain4j.data.document.*;
import dev.langchain4j.data.document.parser.PdfDocumentParser;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.source.UrlSource;
import org.springframework.beans.factory.xml.DocumentLoader;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;

import static com.honvay.flychat.knowledge.infra.document.DocumentType.TXT;


@Component
public class KnowledgeDocumentLoader {

    public Document load(InputStream source, String documentName){
        com.honvay.flychat.knowledge.infra.document.DocumentType documentType = this.detectDocumentType(documentName);
        DocumentParser documentParser = this.parserFor(documentType);
       return load(new StreamDocumentSource(source),documentParser);
    }

    public Document load(File source){
        com.honvay.flychat.knowledge.infra.document.DocumentType documentType = this.detectDocumentType(source.getName());
        DocumentParser documentParser = this.parserFor(documentType);
        try {
            return load(new StreamDocumentSource(new FileInputStream(source)),documentParser);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Document load(URL source){
        com.honvay.flychat.knowledge.infra.document.DocumentType documentType = this.detectDocumentType(source.toString());
        DocumentParser documentParser = this.parserFor(documentType);
        return load(new UrlSource(source),documentParser);
    }

    public Document load(DocumentSource documentSource,DocumentParser documentParser){
        Document document;
        try {
            document = documentParser.parse(documentSource.inputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Metadata sourceMetadata = documentSource.sourceMetadata();
        document.metadata().mergeFrom(sourceMetadata);
        return document;
    }


    private DocumentParser parserFor(com.honvay.flychat.knowledge.infra.document.DocumentType type) {
        return switch (type) {
            case TXT -> new TextDocumentParser();
            case PDF -> new PdfDocumentParser();
            case WORD -> new WordDocumentParser();
        };
    }

    private com.honvay.flychat.knowledge.infra.document.DocumentType detectDocumentType(String name) {
        if (name.endsWith("txt")) {
            return TXT;
        }

        if (name.endsWith("pdf")) {
            return com.honvay.flychat.knowledge.infra.document.DocumentType.PDF;
        }

        if(name.endsWith(".doc") || name.endsWith("docx")){
            return  DocumentType.WORD;
        }

        throw new RuntimeException("Cannot automatically detect the document type for '" + name + "'. Please provide the document type explicitly.");
    }

}
