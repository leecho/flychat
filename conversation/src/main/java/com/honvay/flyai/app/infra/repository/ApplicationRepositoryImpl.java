package com.honvay.flyai.app.infra.repository;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flyai.app.infra.converter.ApplicationConverter;
import com.honvay.flyai.app.infra.mapper.ApplicationMapper;
import com.honvay.flyai.app.infra.po.ApplicationDocumentPo;
import com.honvay.flyai.app.infra.po.ApplicationPo;
import com.honvay.flyai.app.domain.model.Application;
import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.app.domain.repository.ApplicationRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApplicationRepositoryImpl extends ServiceImpl<ApplicationMapper, ApplicationPo> implements ApplicationRepository {

    private final ApplicationConverter converter;

    private final ApplicationDocumentRepositoryImpl applicationKnowledgeRepository;

    public ApplicationRepositoryImpl(ApplicationConverter converter,
                                     ApplicationDocumentRepositoryImpl applicationKnowledgeRepository) {
        this.converter = converter;
        this.applicationKnowledgeRepository = applicationKnowledgeRepository;
    }

    @Override
    public Application get(Long applicationId) {
        ApplicationPo applicationPo = this.getBaseMapper().selectById(applicationId);
        Application application = converter.convert(applicationPo);
        List<Document> documents = this.applicationKnowledgeRepository.findByApplication(application);
        application.getReference().setDocuments(documents);
        return application;
    }

    @Override
    public void save(Application application) {
        ApplicationPo applicationPo = converter.convert(application);
        this.save(applicationPo);
        if(application.hasReference()){
            this.saveDocument(application);
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
        LambdaUpdateWrapper<ApplicationDocumentPo> wrapper = Wrappers.lambdaUpdate(ApplicationDocumentPo.class);
        wrapper.eq(ApplicationDocumentPo::getApplicationId, application.getId());
        this.applicationKnowledgeRepository.remove(wrapper);
    }

    @Override
    public void saveDocument(Application application) {

        List<ApplicationDocumentPo> knowledgeBasePos = application.getReference()
                .getDocuments()
                .stream()
                .map(document -> {
                    ApplicationDocumentPo applicationDocumentPo = new ApplicationDocumentPo();
                    applicationDocumentPo.setApplicationId(application.getId());
                    applicationDocumentPo.setDocumentId(document.getId());
                    return applicationDocumentPo;
                }).toList();

        this.applicationKnowledgeRepository.saveBatch(knowledgeBasePos);
    }

    @Override
    public List<Application> findAll(){
        List<ApplicationPo> list = this.list();
        return list.stream()
                .map(this.converter::convert)
                .toList();
    }

    @Override
    public void deleteDocument(Application application) {

        List<Long> knowledgeBaseIds = application.getReference()
                .getDocuments()
                .stream()
                .map(Document::getId)
                .toList();
        LambdaUpdateWrapper<ApplicationDocumentPo> wrapper = Wrappers.lambdaUpdate(ApplicationDocumentPo.class);
        wrapper.eq(ApplicationDocumentPo::getApplicationId, application.getId())
                .in(ApplicationDocumentPo::getDocumentId,knowledgeBaseIds);

        this.applicationKnowledgeRepository.remove(wrapper);
    }

    @Override
    public void updateReference(Application application) {
        ApplicationPo applicationPo = this.converter.convertReference(application);
        this.updateById(applicationPo);
    }
}
