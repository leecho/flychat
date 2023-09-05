package com.honvay.flyai.document.domain.event;

import org.springframework.context.ApplicationEvent;

public class DocumentCreateEvent extends ApplicationEvent {

    public DocumentCreateEvent(Object source) {
        super(source);
    }
}
