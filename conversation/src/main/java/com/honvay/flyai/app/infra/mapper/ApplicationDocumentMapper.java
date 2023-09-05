package com.honvay.flyai.app.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.flyai.app.infra.po.ApplicationDocumentPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationDocumentMapper extends BaseMapper<ApplicationDocumentPo> {
}
