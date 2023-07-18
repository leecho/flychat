package com.honvay.flychat.knowledge.infra.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.honvay.flychat.knowledge.infra.po.ApplicationPo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ApplicationMapper extends BaseMapper<ApplicationPo> {


}
