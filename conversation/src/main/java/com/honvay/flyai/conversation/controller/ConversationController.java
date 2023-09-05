package com.honvay.flyai.conversation.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.honvay.flyai.conversation.domain.model.Conversation;
import com.honvay.flyai.conversation.domain.model.Message;
import com.honvay.flyai.conversation.domain.repository.ConversationRepository;
import com.honvay.flyai.framework.core.protocol.Response;
import com.honvay.flyai.conversation.application.ConversationApplicationService;
import com.honvay.flyai.framework.web.ResponsePayload;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

@Slf4j
@RestController
@RequestMapping("/conversation")
public class ConversationController {

    private final ConversationApplicationService conversationApplicationService;

    private final ConversationRepository conversationRepository;

    private final ConversationAssembler conversationAssembler;

    private final ObjectMapper objectMapper;

    public ConversationController(ConversationApplicationService conversationApplicationService,
                                  ConversationRepository conversationRepository,
                                  ConversationAssembler conversationAssembler,
                                  ObjectMapper objectMapper) {
        this.conversationApplicationService = conversationApplicationService;
        this.conversationRepository = conversationRepository;
        this.conversationAssembler = conversationAssembler;
        this.objectMapper = objectMapper;
    }

    @ResponsePayload
    @GetMapping
    public List<ConversationVo> list(@RequestParam Long applicationId){
        List<Conversation> conversations = this.conversationRepository.find(applicationId);
        return conversations.stream()
                .map(ConversationVo::from)
                .toList();
    }

    @ResponsePayload
    @GetMapping("/{id}/messages")
    public List<MessageVo> findMessage(@PathVariable Long id){
        List<Message> message = this.conversationRepository.findMessage(id);
        return message.stream()
                .map(MessageVo::from)
                .toList();
    }


    public ResponseEntity<Response<MessageVo>> converse(Conversation conversation,String prompt) {
        Message message = this.conversationApplicationService.converse(conversation,prompt);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Response.success(MessageVo.from(message)));
    }


    @RequestMapping
    public ResponseEntity<SseEmitter> converse(@RequestBody @Validated ConversationDto conversationDto,
                                               HttpServletResponse response) throws IOException {
        Conversation conversation = conversationAssembler.assemble(conversationDto);
        String prompt = conversationDto.getPrompt();
        if (conversationDto.isStream()) {
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
                this.conversationApplicationService.converse(conversation,prompt, onResult, onComplete, onError);
            } catch (Exception e) {
                log.error("调用对话接口发生错误:",e);
                ResponseEntity<Object> responseEntity = ResponseEntity.internalServerError()
                        .contentType(MediaType.APPLICATION_JSON)
                        .body(Response.success("系统错误"));
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
            ResponseEntity<Response<MessageVo>> converse = converse(conversation,prompt);
            this.write(response,converse);
            return null;
        }
    }

    private String wrapMessage(Conversation conversation, String result) throws JsonProcessingException {
        MessageVo vo = new MessageVo();
        vo.setConversationId(conversation.getId());
        vo.setContent(result);
        return objectMapper.writeValueAsString(vo);
    }

    private void write(HttpServletResponse response ,ResponseEntity<?> entity) throws IOException {
        Set<Map.Entry<String, List<String>>> entries = entity.getHeaders().entrySet();
        for (Map.Entry<String, List<String>> entry : entries) {
            for (String s : entry.getValue()) {
                response.addHeader(entry.getKey(),s);
            }
        }
        response.setStatus(entity.getStatusCode().value());
        PrintWriter writer = response.getWriter();
        objectMapper.writeValue(writer,entity.getBody());

    }
}
