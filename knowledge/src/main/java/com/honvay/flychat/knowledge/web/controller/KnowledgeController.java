package com.honvay.flychat.knowledge.web.controller;

import com.honvay.cola.framework.web.ResponsePayload;
import com.honvay.flychat.knowledge.application.KnowledgeApplicationService;
import com.honvay.flychat.knowledge.application.KnowledgeEmbeddingService;
import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.domain.model.KnowledgeItem;
import com.honvay.flychat.knowledge.domain.model.Relevant;
import com.honvay.flychat.knowledge.domain.model.SplitType;
import com.honvay.flychat.knowledge.web.converter.KnowledgeAssembler;
import com.honvay.flychat.knowledge.web.model.dto.KnowledgeDto;
import com.honvay.flychat.knowledge.web.model.dto.KnowledgeItemDto;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@RestController
@ResponsePayload
@RequestMapping("/knowledge")
public class KnowledgeController {

    private final KnowledgeApplicationService knowledgeApplicationService;

    private final KnowledgeEmbeddingService knowledgeEmbeddingService;
    private final KnowledgeAssembler knowledgeAssembler;

    public KnowledgeController(KnowledgeApplicationService knowledgeApplicationService,
                               KnowledgeEmbeddingService knowledgeEmbeddingService,
                               KnowledgeAssembler knowledgeAssembler) {
        this.knowledgeApplicationService = knowledgeApplicationService;
        this.knowledgeEmbeddingService = knowledgeEmbeddingService;
        this.knowledgeAssembler = knowledgeAssembler;
    }

    @PostMapping
    public void create(@RequestBody KnowledgeDto knowledgeDto){
        this.knowledgeApplicationService.create(knowledgeAssembler.assemble(knowledgeDto));
    }

    @PutMapping
    public void update(@RequestBody KnowledgeDto knowledgeDto){
        this.knowledgeApplicationService.update(knowledgeAssembler.assemble(knowledgeDto));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        // TODO: 2023/7/15 删除需要再完善
        // this.knowledgeApplicationService.
    }

    @PostMapping("/item")
    public void createItem(@RequestBody KnowledgeItemDto knowledgeItemDto){
        KnowledgeBase knowledgeBase = KnowledgeBase.of(knowledgeItemDto.getKnowledgeId());
        if(knowledgeItemDto.getType() == KnowledgeItemDto.TEXT_TYPE){
            this.knowledgeApplicationService.addText(knowledgeBase,knowledgeItemDto.getName(),knowledgeItemDto.getSource(), SplitType.of(knowledgeItemDto.getSplitType()));
        }else{
            this.knowledgeApplicationService.addFile(knowledgeBase,new File(knowledgeItemDto.getSource()), SplitType.of(knowledgeItemDto.getSplitType()));
        }
    }

    @DeleteMapping("/{id}/item/{itemId}")
    public void deleteItem(@PathVariable Long id, @PathVariable Long itemId){
        KnowledgeBase knowledgeBase = KnowledgeBase.of(id);
        KnowledgeItem knowledgeItem = KnowledgeItem.of(itemId);
        knowledgeBase.addItem(knowledgeItem);
        this.knowledgeApplicationService.deleteItem(knowledgeBase);
    }

    @GetMapping("/relevant")
    public List<Relevant> findRelevant(@RequestParam String source,
                                       @RequestParam List<Long> knowledgeIds,
                                       @RequestParam(defaultValue = "0.8f") double similarity,
                                       @RequestParam(defaultValue = "5") int relevantSize){
        return this.knowledgeEmbeddingService.findRelevant(source,knowledgeIds,similarity,relevantSize);
    }
}
