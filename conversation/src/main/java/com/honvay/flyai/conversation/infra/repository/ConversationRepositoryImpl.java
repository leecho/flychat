package com.honvay.flyai.conversation.infra.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flyai.conversation.domain.model.Conversation;
import com.honvay.flyai.conversation.domain.model.Message;
import com.honvay.flyai.conversation.domain.model.Quote;
import com.honvay.flyai.conversation.domain.repository.ConversationRepository;
import com.honvay.flyai.conversation.infra.converter.ConversationConverter;
import com.honvay.flyai.conversation.infra.mapper.ConversationMapper;
import com.honvay.flyai.conversation.infra.mapper.MessageMapper;
import com.honvay.flyai.conversation.infra.mapper.MessageQuotaMapper;
import com.honvay.flyai.conversation.infra.po.ConversationPo;
import com.honvay.flyai.conversation.infra.po.MessagePo;
import com.honvay.flyai.conversation.infra.po.QuotePo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ConversationRepositoryImpl extends ServiceImpl<ConversationMapper, ConversationPo> implements ConversationRepository {

    private final MessageMapper messageMapper;

    private final MessageQuotaMapper messageQuotaMapper;

    private final ConversationConverter converter;

    public ConversationRepositoryImpl(MessageMapper messageMapper,
                                      MessageQuotaMapper messageQuotaMapper,
                                      ConversationConverter converter) {
        this.messageMapper = messageMapper;
        this.messageQuotaMapper = messageQuotaMapper;
        this.converter = converter;

    }

    @Override
    @Transactional
    public void saveMessages(Conversation conversation) {
        for (Message message : conversation.getMessages()) {
            MessagePo messagePo = converter.convert(message);
            messagePo.setConversationId(conversation.getId());
            this.messageMapper.insert(messagePo);
            message.setId(messagePo.getId());

            if (message.hasQuotes()) {
                List<Quote> chatQuotes = message.getQuotes();
                for (Quote quote : chatQuotes) {
                    QuotePo quotePo = converter.convert(quote);
                    quotePo.setMessageId(message.getId());
                    this.messageQuotaMapper.insert(quotePo);
                }
            }
        }
    }

    @Override
    public List<Message> findMessage(Long conversationId, Integer limit, Integer size) {
        return null;
    }

    @Override
    public void create(Conversation conversation) {
        ConversationPo conversationPo = converter.convert(conversation);
        this.getBaseMapper().insert(conversationPo);
        conversation.setId(conversationPo.getId());
    }

    @Override
    public List<Message> findMessage(Conversation conversation, int start, int size) {
        LambdaQueryWrapper<MessagePo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MessagePo::getConversationId, conversation.getId())
                .orderByDesc(MessagePo::getCreateTime)
                .last(" limit " + size + " offset " + start);
        List<MessagePo> messagePos = this.messageMapper.selectList(wrapper);
        return messagePos.stream()
                .map(converter::convert)
                .toList();
    }

    @Override
    public List<Conversation> find(Long applicationId) {
        LambdaQueryWrapper<ConversationPo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ConversationPo::getApplicationId,applicationId)
                .orderByDesc(ConversationPo::getCreateTime);
        List<ConversationPo> list = this.list(wrapper);
        return list.stream()
                .map(converter::convert)
                .toList();
    }

    @Override
    public List<Message> findMessage(Long conversationId) {
        LambdaQueryWrapper<MessagePo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(MessagePo::getConversationId, conversationId)
                .orderByAsc(MessagePo::getCreateTime);
        List<MessagePo> chatMessagePos = this.messageMapper.selectList(wrapper);
        return chatMessagePos.stream()
                .map(converter::convert)
                .toList();
    }
}
