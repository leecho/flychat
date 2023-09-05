package com.honvay.flyai.app.infra.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.honvay.flyai.app.infra.mapper.ApplicationDocumentMapper;
import com.honvay.flyai.app.infra.po.ApplicationDocumentPo;
import com.honvay.flyai.app.domain.model.Application;
import com.honvay.flyai.document.domain.model.Document;
import com.honvay.flyai.app.domain.repository.ApplicationDocumentRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ApplicationDocumentRepositoryImpl extends ServiceImpl<ApplicationDocumentMapper, ApplicationDocumentPo> implements ApplicationDocumentRepository {
    @Override
    public List<Document> findByApplication(Application application) {
        LambdaQueryWrapper<ApplicationDocumentPo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(ApplicationDocumentPo::getApplicationId,application.getId());
        return this.getBaseMapper()
                .selectList(wrapper)
                .stream()
                .map(applicationDocumentPo -> Document.of(applicationDocumentPo.getDocumentId()))
                .toList();
    }
}
