package com.honvay.flyai.conversation.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.flyai.conversation.infra.po.QuotePo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MessageQuotaMapper extends BaseMapper<QuotePo> {
}
