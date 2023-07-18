package com.honvay.flychat.knowledge.infra.mapper;

import com.honvay.flychat.knowledge.infra.po.KnowledgeItemPo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description Mapper for KnowledgeItem
 * @createDate ${.now?string('yyyy-MM-dd HH:mm:ss')}
 * @Entity KnowledgeItem
 */
@Mapper
public interface KnowledgeItemMapper extends BaseMapper<KnowledgeItemPo> {


}
