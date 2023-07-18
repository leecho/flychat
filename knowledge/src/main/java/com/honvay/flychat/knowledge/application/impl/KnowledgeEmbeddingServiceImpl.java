package com.honvay.flychat.knowledge.application.impl;

import com.honvay.flychat.knowledge.application.KnowledgeEmbeddingService;
import com.honvay.flychat.knowledge.domain.model.KnowledgeBase;
import com.honvay.flychat.knowledge.domain.model.KnowledgeDetail;
import com.honvay.flychat.knowledge.domain.model.Relevant;
import com.honvay.flychat.knowledge.domain.service.KnowledgeBaseDomainService;
import com.honvay.flychat.knowledge.domain.event.KnowledgeItemCreateEvent;
import com.honvay.flychat.knowledge.domain.repository.KnowledgeDetailRepository;
import com.honvay.flychat.langchain.llama.embedding.EmbeddingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Component
public class KnowledgeEmbeddingServiceImpl implements KnowledgeEmbeddingService {

    private final EmbeddingService embeddingService;

    private final KnowledgeBaseDomainService knowledgeBaseDomainService;


    public KnowledgeEmbeddingServiceImpl(EmbeddingService embeddingService,
                                         KnowledgeBaseDomainService knowledgeBaseDomainService) {
        this.embeddingService = embeddingService;
        this.knowledgeBaseDomainService = knowledgeBaseDomainService;
    }


    @Override
    public  List<Relevant> findRelevant(String source, List<Long> knowledgeIds, Double similarity, int relevantSize){
        float[] embed = this.embeddingService.embed(source);
        return  this.knowledgeBaseDomainService.findRelevantDetails(knowledgeIds, embed, similarity, relevantSize);
    }

    @EventListener(KnowledgeItemCreateEvent.class)
    public void eventHandler(KnowledgeItemCreateEvent event){
        KnowledgeBase source = (KnowledgeBase) event.getSource();
        List<KnowledgeDetail> knowledgeDetails = source.getItems()
                .stream()
                .flatMap(item -> item.getDetails().stream())
                .toList();
        log.info("监听到知识库创建时间，开始进行训练，知识库ID：{}，条目ID：{}",source.getId(),knowledgeDetails.size());
        List<String> texts = knowledgeDetails.stream()
                .map(KnowledgeDetail::getSegment)
                .collect(Collectors.toList());
        List<float[]> embeddings = embeddingService.embed(texts);
        int index = 0;
        for (float[] embedding : embeddings) {
            KnowledgeDetail detail = knowledgeDetails.get(index);
            detail.embed(embedding);
            index ++;
        }
        this.knowledgeBaseDomainService.updateEmbedding(source);
    }

    private boolean checkBalance(){
        return true;
    }

}
