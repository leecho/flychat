package com.honvay.flychat.chat.infra.converter;

import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatMessage;
import com.honvay.flychat.chat.domain.model.ChatQuote;
import com.honvay.flychat.chat.infra.po.ChatMessagePo;
import com.honvay.flychat.chat.infra.po.ChatQuotePo;
import com.honvay.flychat.chat.infra.po.ChatPo;
import org.springframework.stereotype.Component;

@Component
public class ChatConverter {

    public ChatPo convert(Chat chat) {
        ChatPo chatPo = new ChatPo();
        chatPo.setId(chat.getId());
        chatPo.setName(chat.getName());
        chatPo.setApplicationId(chat.getApplicationId());
        chatPo.setUserId(chat.getUser().getId());
        chatPo.setCreateTime(chat.getCreateTime());
        return chatPo;
    }

    public Chat convert(ChatPo chatPo) {
        Chat chat = new Chat();
        chat.setId(chatPo.getId());
        chat.setName(chatPo.getName());
        chat.setCreateTime(chatPo.getCreateTime());
        return chat;
    }

    public ChatMessagePo convert(ChatMessage message) {
        ChatMessagePo chatMessagePo = new ChatMessagePo();
        chatMessagePo.setId(message.getId());
        chatMessagePo.setContent(message.getContent());
        chatMessagePo.setPrompt(message.getPrompt());
        chatMessagePo.setRole(message.getRole());
        chatMessagePo.setTokenSize(message.getTokenSize());
        chatMessagePo.setCost(message.getCost());
        chatMessagePo.setConversationId(message.getConversationId());
        return chatMessagePo;
    }

    public ChatQuotePo convert(ChatQuote quote) {
        ChatQuotePo chatQuotePo = new ChatQuotePo();
        chatQuotePo.setSegment(quote.getSegment());
        chatQuotePo.setSimilarity(quote.getSimilarity());
        chatQuotePo.setSourceId(quote.getSourceId());
        return chatQuotePo;
    }

    public ChatMessage convert(ChatMessagePo chatMessagePo) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setId(chatMessagePo.getId());
        chatMessage.setContent(chatMessagePo.getContent());
        chatMessage.setPrompt(chatMessagePo.getPrompt());
        chatMessage.setRole(chatMessagePo.getRole());
        chatMessage.setTokenSize(chatMessagePo.getTokenSize());
        chatMessage.setCost(chatMessagePo.getCost());
        chatMessage.setCreateTime(chatMessagePo.getCreateTime());
        return chatMessage;
    }

}
