package com.honvay.flychat.knowledge.domain.event;

import org.springframework.context.ApplicationEvent;

public class KnowledgeItemCreateEvent extends ApplicationEvent {

    public KnowledgeItemCreateEvent(Object source) {
        super(source);
    }
}
