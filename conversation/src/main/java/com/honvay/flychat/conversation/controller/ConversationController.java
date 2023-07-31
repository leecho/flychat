package com.honvay.flychat.conversation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honvay.cola.framework.core.protocol.Response;
import com.honvay.flychat.conversation.application.ConversationApplicationService;
import com.honvay.flychat.conversation.domain.Conversation;
import jakarta.servlet.http.HttpServletResponse;
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
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@RestController
@RequestMapping("/conversation")
public class ConversationController {

    private final ConversationApplicationService conversationApplicationService;

    private final ObjectMapper objectMapper;

    public ConversationController(ConversationApplicationService conversationApplicationService,
                                  ObjectMapper objectMapper) {
        this.conversationApplicationService = conversationApplicationService;
        this.objectMapper = objectMapper;
    }

    public ResponseEntity<Response<ConversationVo>> converse(Conversation conversation) {
        this.conversationApplicationService.converse(conversation);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response.success(ConversationVo.from(conversation)));
    }

    @RequestMapping
    public ResponseEntity<SseEmitter> converse(@RequestBody Conversation conversation, HttpServletResponse response) throws IOException {
        conversation.init();
        if (conversation.isStream()) {
            SseEmitter emitter = new SseEmitter(3600L * 6);
            Consumer<String> onResult = text -> {
                try {
                    emitter.send(wrapMessage(conversation,text));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            };
            Consumer<Void> onComplete = unused -> {
                try {
                    emitter.send(SseEmitter.event().name("complete").data("complete"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                emitter.complete();
            };
            Consumer<Throwable> onError = emitter::completeWithError;
            try {
                this.conversationApplicationService.converse(conversation, onResult, onComplete, onError);
            } catch (Exception e) {
                String message = Optional.ofNullable(e.getMessage())
                        .orElse(e.getClass().getSimpleName());
                e.printStackTrace();
                ResponseEntity<Object> responseEntity = ResponseEntity.internalServerError()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Response.success(message));
                this.write(response,responseEntity);
                return null;
            }
            HttpHeaders headers = new HttpHeaders();
            headers.set("X-Accel-Buffering", "no");
            headers.setCacheControl(CacheControl.noCache());
            return ResponseEntity.ok()
                    .contentType(MediaType.TEXT_EVENT_STREAM)
                    .headers(headers)
                    .body(emitter);
        } else {
            ResponseEntity<Response<ConversationVo>> converse = converse(conversation);
            this.write(response,converse);
            return null;
        }
    }

    private String wrapMessage(Conversation conversation, String result) throws JsonProcessingException {
        ConversationVo vo = ConversationVo.from(conversation);
        vo.setMessage(result);
        return objectMapper.writeValueAsString(vo);
    }

    private void write(HttpServletResponse httpServletResponse ,ResponseEntity<?> entity) throws IOException {
        Set<Map.Entry<String, List<String>>> entries = entity.getHeaders().entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            for (String s : entry.getValue()) {
                httpServletResponse.addHeader(entry.getKey(),s);
            }
        }
        httpServletResponse.setStatus(entity.getStatusCode().value());
        PrintWriter writer = httpServletResponse.getWriter();
        objectMapper.writeValue(writer,entity.getBody());

    }
}
