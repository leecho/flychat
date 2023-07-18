package com.honvay.flychat.knowledge.infra.mapper;

import com.honvay.flychat.knowledge.infra.po.KnowledgeBasePo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description Mapper for Knowledge
 * @createDate ${.now?string('yyyy-MM-dd HH:mm:ss')}
 * @Entity Knowledge
 */
@Mapper
public interface KnowledgeBaseMapper extends BaseMapper<KnowledgeBasePo> {


}
