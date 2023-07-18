package com.honvay.flychat.knowledge.infra.factory;

import com.honvay.flychat.knowledge.domain.model.*;
import com.honvay.flychat.knowledge.infra.po.KnowledgeDetailPo;
import com.honvay.flychat.knowledge.infra.po.KnowledgeBasePo;
import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.infra.po.KnowledgeItemPo;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeConverter {

    public KnowledgeBasePo convert(KnowledgeBase knowledgeBase) {
        KnowledgeBasePo knowledgeBasePo = new KnowledgeBasePo();
        knowledgeBasePo.setId(knowledgeBase.getId());
        knowledgeBasePo.setName(knowledgeBase.getName());
        knowledgeBasePo.setTags(knowledgeBase.getTags());
        knowledgeBasePo.setLogo(knowledgeBase.getLogo());
        knowledgeBasePo.setUserId(knowledgeBase.getOwner().getId());
        return knowledgeBasePo;
    }

    public KnowledgeItemPo convert(KnowledgeItem knowledgeItem) {
        KnowledgeItemPo knowledgeItemPo = new KnowledgeItemPo();
        knowledgeItemPo.setSplitType(knowledgeItem.getSplitType().getCode());
        //knowledgeItemPo.setSplitStep();
        knowledgeItemPo.setSource(knowledgeItem.getName());
        knowledgeItemPo.setId(knowledgeItemPo.getId());

        return knowledgeItemPo;
    }

    public KnowledgeDetailPo convert(KnowledgeDetail knowledgeDetail){
        KnowledgeDetailPo knowledgeDetailPo = new KnowledgeDetailPo();
        knowledgeDetailPo.setId(knowledgeDetail.getId());
        knowledgeDetailPo.setSource(knowledgeDetail.getSource());
        knowledgeDetailPo.setSegment(knowledgeDetail.getSegment());
        knowledgeDetailPo.setEmbedding(knowledgeDetail.getEmbedding());
        knowledgeDetailPo.setStatus(knowledgeDetail.getStatus().getCode());
        return knowledgeDetailPo;
    }

    public KnowledgeBase convert(KnowledgeBasePo knowledgeBasePo){
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setId(knowledgeBasePo.getId());
        knowledgeBase.setName(knowledgeBasePo.getName());
        knowledgeBase.setTags(knowledgeBasePo.getTags());
        knowledgeBase.setLogo(knowledgeBasePo.getTags());
        Owner knowledgeOwner = new Owner();
        knowledgeOwner.setId(knowledgeBase.getId());
        knowledgeBase.setOwner(knowledgeOwner);
        return knowledgeBase;
    }

    /**
     * @param knowledgeDetailPo
     * @return
     */
    public KnowledgeDetail convert(KnowledgeDetailPo knowledgeDetailPo){
        KnowledgeDetail knowledgeDetail = new KnowledgeDetail();
        knowledgeDetail.setId(knowledgeDetailPo.getId());
        knowledgeDetail.setEmbedding(knowledgeDetailPo.getEmbedding());
        knowledgeDetail.setSource(knowledgeDetailPo.getSource());
        knowledgeDetail.setSegment(knowledgeDetailPo.getSegment());
        knowledgeDetail.setStatus(KnowledgeItemStatus.of(knowledgeDetailPo.getStatus()));
        return knowledgeDetail;
    }

}
