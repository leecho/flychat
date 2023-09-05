package com.honvay.flyai.conversation.application.provider;

import com.honvay.flyai.app.domain.model.Reference;
import com.honvay.flyai.conversation.domain.model.Conversation;
import com.honvay.flyai.conversation.domain.model.Message;
import com.honvay.flyai.conversation.domain.model.Quote;
import com.honvay.flyai.document.application.DocumentApplicationService;
import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.document.domain.model.RelevantSegment;
import com.honvay.flyai.langchain.chat.ChatModelService;
import dev.langchain4j.model.input.Prompt;
import dev.langchain4j.model.input.PromptTemplate;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.joining;

@Component
public class DocumentMessageProvider implements MessageProvider {

    private final DocumentApplicationService documentApplicationService;

    public DocumentMessageProvider(DocumentApplicationService documentApplicationService) {
        this.documentApplicationService = documentApplicationService;
    }

    @Override
    public void setupUserMessage(Conversation conversation, Message message) {
        List<RelevantSegment> relevantSegment = this.findRelevant(conversation,message);
        String promptText = buildPromptText(message.getPrompt(), relevantSegment);
        List<Quote> quotes = relevantSegment.stream()
                .map(this::convert)
                .toList();
        message.setPrompt(promptText);
        message.setQuotes(quotes);
    }

    private Quote convert(RelevantSegment segment) {
        Quote quote = new Quote();
        quote.setSimilarity(segment.getSimilarity());
        quote.setDocumentId(segment.getDocumentId());
        quote.setDocument(segment.getDocumentName());
        quote.setSegment(segment.getSegment());
        return quote;

    }

    @NotNull
    @Override
    public List<Message> getRelationMessages(Conversation conversation) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "knowledge";
    }


    private String buildPromptText(String question, List<RelevantSegment> relevantSegment) {
        String templateText = this.getPromptTemplateText();
        Map<String, Object> variables = this.getPromptVariables(question, relevantSegment);
        PromptTemplate promptTemplate = PromptTemplate.from(templateText);
        Prompt prompt = promptTemplate.apply(variables);
        return prompt.text();
    }

    private List<RelevantSegment> findRelevant(Conversation conversation,Message message) {
        Reference reference = conversation.getApplication().getReference();
        List<Long> documentIds = reference.getDocuments()
                .stream()
                .map(Document::getId)
                .toList();
        return documentApplicationService.findRelevantSegment(message.getPrompt(), documentIds, reference.getSimilarity(), reference.getRelevantSize());
    }

    private String getPromptTemplateText() {
        return "Answer the following question to the best of your ability:\n"
                + "\n"
                + "Question:\n"
                + "{{question}}\n"
                + "\n"
                + "Base your answer on the following information:\n"
                + "{{information}}";
    }

    private Map<String, Object> getPromptVariables(String question, List<RelevantSegment> relevantSegment) {

        String information = relevantSegment.stream()
                .map(RelevantSegment::getSegment)
                .collect(joining("\n\n"));
        Map<String, Object> variables = new HashMap<>();
        variables.put("question", question);
        variables.put("information", information);
        return variables;
    }
}
