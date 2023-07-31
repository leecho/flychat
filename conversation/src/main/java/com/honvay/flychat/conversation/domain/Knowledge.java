package com.honvay.flychat.conversation.domain;

import com.honvay.flychat.chat.domain.model.ChatQuote;
import lombok.Data;

import java.util.List;

@Data
public class Knowledge {

    /**
     * 知识库列表
     */
    private List<Long> knowledgeBases;

    /**
     * 相似度
     */
    private Double similarity;

    /**
     * 关联数量
     */
    private Integer relevantSize;

    /**
     * 引用
     */
    private List<ChatQuote> quotes;

    private boolean blockOnEmpty;

}
