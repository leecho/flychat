package com.honvay.flychat.chat.web;

import com.honvay.cola.framework.web.ResponsePayload;
import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatMessage;
import com.honvay.flychat.chat.domain.repository.ChatRepository;
import com.honvay.flychat.chat.web.model.ChatMessageVo;
import com.honvay.flychat.chat.web.model.ChatVo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@ResponsePayload
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatRepository chatRepository;

    public ChatController(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @GetMapping
    public List<ChatVo> list(){
        List<Chat> chats = this.chatRepository.find();
        return chats.stream()
                .map(ChatVo::from)
                .toList();
    }

    @GetMapping("/{id}/messages")
    public List<ChatMessageVo> findMessage(@PathVariable Long id){
        Chat chat = new Chat();
        chat.setId(id);
        List<ChatMessage> chatMessages = this.chatRepository.findMessage(chat);
        return chatMessages.stream()
                .map(ChatMessageVo::from)
                .toList();
    }

}
