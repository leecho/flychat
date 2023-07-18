package com.honvay.flychat.knowledge.web.controller;

import com.honvay.cola.framework.core.exception.ServiceException;
import com.honvay.cola.framework.web.ResponsePayload;
import com.honvay.flychat.knowledge.application.KnowledgeChatService;
import com.honvay.flychat.knowledge.domain.model.KnowledgeChat;
import com.honvay.flychat.knowledge.web.converter.KnowledgeChatAssembler;
import com.honvay.flychat.knowledge.web.model.dto.KnowledgeChatDto;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.function.Consumer;

@RestController
@RequestMapping("/knowledge/chat")
public class KnowledgeChatController {

    private final KnowledgeChatService knowledgeChatService;

    private final KnowledgeChatAssembler knowledgeChatAssembler;

    public KnowledgeChatController(KnowledgeChatService knowledgeChatService,
                                   KnowledgeChatAssembler knowledgeChatAssembler) {
        this.knowledgeChatService = knowledgeChatService;
        this.knowledgeChatAssembler = knowledgeChatAssembler;
    }

    @ResponsePayload
    @RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public String chat(KnowledgeChatDto knowledgeChatDto){

        KnowledgeChat knowledgeChat = assembleChat(knowledgeChatDto);

        return this.knowledgeChatService.chat(knowledgeChat, knowledgeChatDto.getQuestion());
    }

    private KnowledgeChat assembleChat(KnowledgeChatDto knowledgeChatDto){
        if(knowledgeChatDto.getChatId() == null && knowledgeChatDto.getApplicationId() == null){
            throw new ServiceException("参数错误");
        }

        return knowledgeChatAssembler.assemble(knowledgeChatDto);
    }

    @RequestMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> asyncChat(KnowledgeChatDto knowledgeChatDto) {

        KnowledgeChat knowledgeChat = assembleChat(knowledgeChatDto);

        SseEmitter sseEmitter = new SseEmitter(3600L * 6);
        Consumer<String> onResult = text -> {
            try {
                sseEmitter.send(text);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        Consumer<Void> onComplete = unused -> {
            try {
                sseEmitter.send(SseEmitter.event().name("complete").data("done"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            sseEmitter.complete();
            System.out.println("已完成");
        };
        Consumer<Throwable> onError = sseEmitter::completeWithError;

        knowledgeChatService.chat(knowledgeChat, knowledgeChatDto.getQuestion(), onResult, onComplete, onError);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("X-Accel-Buffering", "no");
        httpHeaders.setCacheControl(CacheControl.noCache());
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_EVENT_STREAM)
                .headers(httpHeaders)
                .body(sseEmitter);
    }
}
