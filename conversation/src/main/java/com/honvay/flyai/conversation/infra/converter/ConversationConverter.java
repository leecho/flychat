package com.honvay.flyai.conversation.infra.converter;

import com.honvay.flyai.conversation.domain.model.Conversation;
import com.honvay.flyai.conversation.domain.model.Message;
import com.honvay.flyai.conversation.domain.model.Quote;
import com.honvay.flyai.conversation.infra.po.ConversationPo;
import com.honvay.flyai.conversation.infra.po.MessagePo;
import com.honvay.flyai.conversation.infra.po.QuotePo;
import org.springframework.stereotype.Component;

@Component
public class ConversationConverter {

    public ConversationPo convert(Conversation conversation) {
        ConversationPo conversationPo = new ConversationPo();
        conversationPo.setId(conversation.getId());
        conversationPo.setName(conversation.getName());
        conversationPo.setApplicationId(conversation.getApplication().getId());
        conversationPo.setUserId(conversation.getUserId());
        return conversationPo;
    }

    public Conversation convert(ConversationPo conversationPo) {
        Conversation conversation = new Conversation();
        conversation.setId(conversationPo.getId());
        conversation.setName(conversationPo.getName());
        return conversation;
    }

    public MessagePo convert(Message message) {
        MessagePo chatMessagePo = new MessagePo();
        chatMessagePo.setId(message.getId());
        chatMessagePo.setContent(message.getContent());
        chatMessagePo.setPrompt(message.getPrompt());
        chatMessagePo.setRole(message.getRole());
        chatMessagePo.setTokenSize(message.getTokenSize());
        return chatMessagePo;
    }

    public QuotePo convert(Quote quote) {
        QuotePo chatQuotePo = new QuotePo();
        chatQuotePo.setSegment(quote.getSegment());
        chatQuotePo.setSimilarity(quote.getSimilarity());
        chatQuotePo.setDocument(quote.getDocument());
        chatQuotePo.setSegment(quote.getSegment());
        return chatQuotePo;
    }

    public Message convert(MessagePo chatMessagePo) {
        Message chatMessage = new Message();
        chatMessage.setId(chatMessagePo.getId());
        chatMessage.setContent(chatMessagePo.getContent());
        chatMessage.setPrompt(chatMessagePo.getPrompt());
        chatMessage.setRole(chatMessagePo.getRole());
        chatMessage.setTokenSize(chatMessagePo.getTokenSize());
        chatMessage.setCreateTime(chatMessagePo.getCreateTime());
        return chatMessage;
    }

}
