package com.honvay.flychat.knowledge.web.converter;

import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.domain.model.KnowledgeItem;
import com.honvay.flychat.knowledge.domain.model.Owner;
import com.honvay.flychat.knowledge.domain.model.SplitType;
import com.honvay.flychat.knowledge.web.model.dto.KnowledgeDto;
import com.honvay.flychat.knowledge.web.model.dto.KnowledgeItemDto;
import org.springframework.stereotype.Component;

@Component
public class KnowledgeAssembler {

    public KnowledgeBase assemble(KnowledgeDto knowledgeDto){
        KnowledgeBase knowledgeBase = new KnowledgeBase();
        knowledgeBase.setId(knowledgeDto.getId());
        knowledgeBase.setName(knowledgeDto.getName());
        knowledgeBase.setTags(knowledgeDto.getTags());
        knowledgeBase.setLogo(knowledgeDto.getLogo());
        Owner owner = new Owner();
        owner.setId(1L);
        knowledgeBase.setOwner(owner);
        return knowledgeBase;
    }

    public KnowledgeItem getKnowledgeItem(KnowledgeItemDto knowledgeItemDto){
        KnowledgeItem knowledgeItem = new KnowledgeItem();
        knowledgeItem.setId(knowledgeItemDto.getId());
        knowledgeItem.setName(knowledgeItemDto.getName());
        knowledgeItem.setSplitType(SplitType.of(knowledgeItemDto.getSplitType()));
        return knowledgeItem;
    }

}
