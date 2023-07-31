package com.honvay.flychat.conversation.domain;

import lombok.Data;

/**
 * 对话上下文
 */
@Data
public class Relation {

    /**
     * 历史消息数量
     */
    private Integer size;

    /**
     * 是否为清洁模式，不关联历史消息
     */
    private boolean clean;
}
