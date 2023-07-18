package com.honvay.flychat.knowledge.infra.mapper;

import com.honvay.flychat.knowledge.domain.model.Relevant;
import com.honvay.flychat.knowledge.infra.po.KnowledgeDetailPo;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pgvector.PGvector;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @description Mapper for KnowledgeDetailPo
 * @createDate ${.now?string('yyyy-MM-dd HH:mm:ss')}
 * @Entity KnowledgeDetailPo
 */
@Mapper
public interface KnowledgeDetailMapper extends BaseMapper<KnowledgeDetailPo> {

    List<Relevant> findRelevant(@Param("knowledgeIds") List<Long> knowledgeIds,
                                @Param("embedding") PGvector embedding,
                                @Param("similarity") Double similarity,
                                @Param("size") int size);

}
