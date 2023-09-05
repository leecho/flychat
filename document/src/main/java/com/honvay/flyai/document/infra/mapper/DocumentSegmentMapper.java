package com.honvay.flyai.document.infra.mapper;

import com.honvay.flyai.document.domain.model.RelevantSegment;
import com.honvay.flyai.document.infra.po.DocumentSegmentPo;

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
public interface DocumentSegmentMapper extends BaseMapper<DocumentSegmentPo> {

    List<RelevantSegment> findRelevant(@Param("documentIds") List<Long> documentIds,
                                       @Param("embedding") PGvector embedding,
                                       @Param("similarity") Double similarity,
                                       @Param("size") int size);

}
