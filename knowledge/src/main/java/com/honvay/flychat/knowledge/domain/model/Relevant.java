package com.honvay.flychat.knowledge.domain.model;

import com.honvay.flychat.chat.domain.model.ChatQuote;
import lombok.Data;

@Data
public class Relevant {

    private Long detailId;

    private String segment;

    private Double similarity;

    private String source;

    private Long knowledgeId;

    private String knowledgeName;

    public ChatQuote toQuote(){
        ChatQuote chatQuote = new ChatQuote();
        chatQuote.setSegment(this.segment);
        chatQuote.setSimilarity(similarity);
        chatQuote.setSourceId(detailId);
        return chatQuote;
    }

}
