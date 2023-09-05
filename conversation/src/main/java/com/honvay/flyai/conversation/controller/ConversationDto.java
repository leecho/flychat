package com.honvay.flyai.conversation.controller;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConversationDto {

    private Long conversationId;

    private Long applicationId;

    @NotBlank(message = "提问内容不能为空")
    private String prompt;

    private boolean stream = false;

}
