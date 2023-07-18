package com.honvay.flychat.knowledge.domain.model;

import com.honvay.flychat.chat.domain.model.Chat;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class KnowledgeChat extends Chat {

    private Application application;

    @Override
    public Long getApplicationId() {
        return application.getId();
    }
}
