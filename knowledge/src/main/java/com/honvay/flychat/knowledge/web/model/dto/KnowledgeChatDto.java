package com.honvay.flychat.knowledge.web.model.dto;

import lombok.Data;

@Data
public class KnowledgeChatDto {

    private Long chatId;

    private Long applicationId;

    private String question;

    private Long userId;
}
