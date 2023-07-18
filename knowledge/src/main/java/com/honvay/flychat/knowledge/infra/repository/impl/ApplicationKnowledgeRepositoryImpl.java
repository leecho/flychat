package com.honvay.flychat.knowledge.infra.repository.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flychat.knowledge.domain.model.Application;
import com.honvay.flychat.knowledge.domain.model.ApplicationKnowledge;
import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.domain.repository.ApplicationKnowledgeRepository;
import com.honvay.flychat.knowledge.infra.mapper.ApplicationKnowledgeMapper;
import com.honvay.flychat.knowledge.infra.po.ApplicationKnowledgePo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApplicationKnowledgeRepositoryImpl extends ServiceImpl<ApplicationKnowledgeMapper, ApplicationKnowledgePo> implements ApplicationKnowledgeRepository {
    @Override
    public List<KnowledgeBase> findByApplication(Application application) {
        LambdaQueryWrapper<ApplicationKnowledgePo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ApplicationKnowledgePo::getApplicationId,application.getId());
        return this.getBaseMapper()
                .selectList(wrapper)
                .stream()
                .map(applicationKnowledgePo -> KnowledgeBase.of(applicationKnowledgePo.getKnowledgeBaseId()))
                .toList();
    }
}
