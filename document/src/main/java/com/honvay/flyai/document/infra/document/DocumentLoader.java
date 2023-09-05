package com.honvay.flyai.document.infra.document;

import com.honvay.flyai.langchain.document.StreamDocumentSource;
import com.honvay.flyai.langchain.parser.WordDocumentParser;
import dev.langchain4j.data.document.*;
import dev.langchain4j.data.document.parser.PdfDocumentParser;
import dev.langchain4j.data.document.parser.TextDocumentParser;
import dev.langchain4j.data.document.source.UrlSource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.net.URL;

import static com.honvay.flyai.document.infra.document.DocumentFileType.TXT;


@Component
public class DocumentLoader {

    public Document load(InputStream source, String documentName){
        DocumentFileType documentFileType = this.detectDocumentType(documentName);
        DocumentParser documentParser = this.parserFor(documentFileType);
       return load(new StreamDocumentSource(source),documentParser);
    }

    public Document load(File source){
        DocumentFileType documentFileType = this.detectDocumentType(source.getName());
        DocumentParser documentParser = this.parserFor(documentFileType);
        try {
            return load(new StreamDocumentSource(new FileInputStream(source)),documentParser);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Document load(URL source){
        DocumentFileType documentFileType = this.detectDocumentType(source.toString());
        DocumentParser documentParser = this.parserFor(documentFileType);
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


    private DocumentParser parserFor(DocumentFileType type) {
        return switch (type) {
            case TXT -> new TextDocumentParser();
            case PDF -> new PdfDocumentParser();
            case WORD -> new WordDocumentParser();
        };
    }

    private DocumentFileType detectDocumentType(String name) {
        if (name.endsWith("txt")) {
            return TXT;
        }

        if (name.endsWith("pdf")) {
            return DocumentFileType.PDF;
        }

        if(name.endsWith(".doc") || name.endsWith("docx")){
            return  DocumentFileType.WORD;
        }

        throw new RuntimeException("Cannot automatically detect the document type for '" + name + "'. Please provide the document type explicitly.");
    }

}
