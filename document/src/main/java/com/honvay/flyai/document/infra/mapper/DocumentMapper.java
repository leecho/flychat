package com.honvay.flyai.document.infra.mapper;

import com.honvay.flyai.document.infra.po.DocumentPo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @description Mapper for KnowledgeItem
 * @createDate ${.now?string('yyyy-MM-dd HH:mm:ss')}
 * @Entity KnowledgeItem
 */
@Mapper
public interface DocumentMapper extends BaseMapper<DocumentPo> {

}
