package com.honvay.flychat.chat.domain.model;

import lombok.Data;

@Data
public class ChatQuote {

    private Double similarity;

    private Long sourceId;

    private String segment;

}
