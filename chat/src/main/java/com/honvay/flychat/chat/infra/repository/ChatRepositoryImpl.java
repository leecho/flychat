package com.honvay.flychat.chat.infra.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flychat.chat.domain.model.Chat;
import com.honvay.flychat.chat.domain.model.ChatMessage;
import com.honvay.flychat.chat.domain.model.ChatQuote;
import com.honvay.flychat.chat.domain.repository.ChatRepository;
import com.honvay.flychat.chat.infra.converter.ChatConverter;
import com.honvay.flychat.chat.infra.mapper.ChatMapper;
import com.honvay.flychat.chat.infra.mapper.ChatMessageMapper;
import com.honvay.flychat.chat.infra.mapper.ChatQuoteMapper;
import com.honvay.flychat.chat.infra.po.ChatMessagePo;
import com.honvay.flychat.chat.infra.po.ChatQuotePo;
import com.honvay.flychat.chat.infra.po.ChatPo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ChatRepositoryImpl extends ServiceImpl<ChatMapper, ChatPo> implements ChatRepository {

    private final ChatConverter converter;

    private final ChatMessageMapper chatMessageMapper;

    private final ChatQuoteMapper chatQuoteMapper;

    public ChatRepositoryImpl(ChatConverter converter,
                              ChatMessageMapper chatMessageMapper,
                              ChatQuoteMapper chatQuoteMapper) {
        this.converter = converter;
        this.chatMessageMapper = chatMessageMapper;
        this.chatQuoteMapper = chatQuoteMapper;
    }

    @Override
    public void create(Chat chat) {
        ChatPo chatPo = converter.convert(chat);
        this.getBaseMapper().insert(chatPo);
        chat.setId(chatPo.getId());
    }

    @Override
    public List<ChatMessage> findMessage(Chat chat, int start, int size) {
        LambdaQueryWrapper<ChatMessagePo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ChatMessagePo::getChatId, chat.getId())
                .orderByDesc(ChatMessagePo::getCreateTime)
                .last(" limit " + size + " offset " + start);
        List<ChatMessagePo> chatMessagePos = this.chatMessageMapper.selectList(wrapper);
        return chatMessagePos.stream()
                .map(converter::convert)
                .toList();
    }

    @Override
    @Transactional
    public void saveMessages(Chat knowledgeChat) {
        for (ChatMessage message : knowledgeChat.getMessages()) {
            ChatMessagePo chatMessagePo = converter.convert(message);
            chatMessagePo.setChatId(knowledgeChat.getId());
            this.chatMessageMapper.insert(chatMessagePo);
            message.setId(chatMessagePo.getId());

            if (message.hasQuotes()) {
                List<ChatQuote> chatQuotes = message.getQuotes();
                for (ChatQuote chatQuote : chatQuotes) {
                    ChatQuotePo chatQuotePo = converter.convert(chatQuote);
                    chatQuotePo.setMessageId(message.getId());
                    this.chatQuoteMapper.insert(chatQuotePo);
                }
            }

        }
    }

    @Override
    public List<Chat> find() {
        LambdaQueryWrapper<ChatPo> wrapper = Wrappers.lambdaQuery();
        wrapper.last(" limit 20")
                .orderByDesc(ChatPo::getCreateTime);
        List<ChatPo> list = this.list(wrapper);
        return list.stream()
                .map(converter::convert)
                .toList();
    }

    @Override
    public List<ChatMessage> findMessage(Chat chat) {
        LambdaQueryWrapper<ChatMessagePo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ChatMessagePo::getChatId, chat.getId())
                .orderByAsc(ChatMessagePo::getCreateTime);
        List<ChatMessagePo> chatMessagePos = this.chatMessageMapper.selectList(wrapper);
        return chatMessagePos.stream()
                .map(converter::convert)
                .toList();
    }
}
