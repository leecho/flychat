package com.honvay.flychat.knowledge.infra.repository.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flychat.knowledge.domain.model.Application;
import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.domain.repository.ApplicationRepository;
import com.honvay.flychat.knowledge.infra.factory.ApplicationConverter;
import com.honvay.flychat.knowledge.infra.mapper.ApplicationMapper;
import com.honvay.flychat.knowledge.infra.po.ApplicationKnowledgePo;
import com.honvay.flychat.knowledge.infra.po.ApplicationPo;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApplicationRepositoryImpl extends ServiceImpl<ApplicationMapper, ApplicationPo> implements ApplicationRepository {

    private final ApplicationConverter converter;

    private final ApplicationKnowledgeRepositoryImpl applicationKnowledgeRepository;

    public ApplicationRepositoryImpl(ApplicationConverter converter,
                                     ApplicationKnowledgeRepositoryImpl applicationKnowledgeRepository) {
        this.converter = converter;
        this.applicationKnowledgeRepository = applicationKnowledgeRepository;
    }

    @Override
    public Application get(Long applicationId) {
        ApplicationPo applicationPo = this.getBaseMapper().selectById(applicationId);
        Application application = converter.convert(applicationPo);
        List<KnowledgeBase> knowledgeBases = this.applicationKnowledgeRepository.findByApplication(application);
        application.getKnowledge().setKnowledgeBases(knowledgeBases);
        return application;
    }

    @Override
    public void save(Application application) {
        ApplicationPo applicationPo = converter.convert(application);
        this.save(applicationPo);
        if(application.hasKnowledge()){
            this.saveKnowledgeBase(application);
        }
        application.setId(applicationPo.getId());
    }

    @Override
    public void update(Application application) {
        ApplicationPo applicationPo = converter.convertBase(application);
        this.updateById(applicationPo);
    }

    @Override
    public void delete(Application application) {
        this.removeById(application.getId());
        LambdaUpdateWrapper<ApplicationKnowledgePo> wrapper = Wrappers.lambdaUpdate(ApplicationKnowledgePo.class);
        wrapper.eq(ApplicationKnowledgePo::getApplicationId, application.getId());
        this.applicationKnowledgeRepository.remove(wrapper);
    }

    @Override
    public void saveKnowledgeBase(Application application) {

        List<ApplicationKnowledgePo> knowledgeBasePos = application.getKnowledge()
                .getKnowledgeBases()
                .stream()
                .map(knowledgeBase -> {
                    ApplicationKnowledgePo applicationKnowledgePo = new ApplicationKnowledgePo();
                    applicationKnowledgePo.setApplicationId(application.getId());
                    applicationKnowledgePo.setKnowledgeBaseId(knowledgeBase.getId());
                    return applicationKnowledgePo;
                }).toList();

        this.applicationKnowledgeRepository.saveBatch(knowledgeBasePos);
    }

    @Override
    public void deleteKnowledgeBase(Application application) {

        List<Long> knowledgeBaseIds = application.getKnowledge()
                .getKnowledgeBases()
                .stream()
                .map(KnowledgeBase::getId)
                .toList();
        LambdaUpdateWrapper<ApplicationKnowledgePo> wrapper = Wrappers.lambdaUpdate(ApplicationKnowledgePo.class);
        wrapper.eq(ApplicationKnowledgePo::getApplicationId, application.getId())
                .in(ApplicationKnowledgePo::getKnowledgeBaseId,knowledgeBaseIds);

        this.applicationKnowledgeRepository.remove(wrapper);
    }

    @Override
    public void updateKnowledge(Application application) {
        ApplicationPo applicationPo = this.converter.convertKnowledge(application);
        this.updateById(applicationPo);
    }
}
