package com.honvay.flychat.knowledge.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.flychat.knowledge.infra.po.ApplicationKnowledgePo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationKnowledgeMapper extends BaseMapper<ApplicationKnowledgePo> {
}
