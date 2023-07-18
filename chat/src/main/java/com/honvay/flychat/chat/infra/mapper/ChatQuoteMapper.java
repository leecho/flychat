package com.honvay.flychat.chat.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.flychat.chat.infra.po.ChatQuotePo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ChatQuoteMapper extends BaseMapper<ChatQuotePo> {
}
