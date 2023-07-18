package com.honvay.flychat.conversation.controller;

import com.honvay.cola.framework.web.ResponsePayload;
import com.honvay.flychat.conversation.application.ConversationApplicationService;
import com.honvay.flychat.conversation.domain.Conversation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.function.Consumer;

@Slf4j
@RestController
@RequestMapping("/conversation")
public class ConversationController {

    private final ConversationApplicationService conversationApplicationService;

    public ConversationController(ConversationApplicationService conversationApplicationService) {
        this.conversationApplicationService = conversationApplicationService;
    }

    @ResponsePayload
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ConversationVo converse(@RequestBody Conversation conversation) {
        conversation.init();
        this.conversationApplicationService.converse(conversation);
        return ConversationVo.from(conversation);
    }

    @RequestMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> asyncConverse(@RequestBody Conversation conversation) throws IOException {
        conversation.init();
        SseEmitter emitter = new SseEmitter(3600L * 6);
        Consumer<String> onResult = text -> {
            try {
                emitter.send(text.replaceAll(" ", "&nbsp;"));
                log.info("对话内容，对话ID：{}，内容:{}，内容长度:{}", conversation.getId(), text, text.length());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Consumer<Void> onComplete = unused -> {
            try {
                emitter.send(SseEmitter.event().name("complete").data("done"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            emitter.complete();
            log.info("对话已完成，对话ID：{}", conversation.getId());
        };
        Consumer<Throwable> onError = emitter::completeWithError;
        conversationApplicationService.converse(conversation, onResult, onComplete, onError);
        if (conversation.getChatId() == null) {
            emitter.send(SseEmitter.event().name("chat").data(conversation.getChatId()));
        }
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Accel-Buffering", "no");
        headers.setCacheControl(CacheControl.noCache());
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .headers(headers)
                .body(emitter);
    }
}
