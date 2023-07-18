package com.honvay.flychat.knowledge.domain.model;

import com.honvay.cola.framework.core.enums.GeneralEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum KnowledgeItemStatus implements GeneralEnum<Integer> {
    PENDING(1,"PENDING"),
    DONE(2,"DONE");


    @Getter
    private final Integer code;

    @Getter
    private final String name;

    KnowledgeItemStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer,KnowledgeItemStatus> mapping;

    static {
        mapping = Arrays.stream(KnowledgeItemStatus.values())
                .collect(Collectors.toMap(KnowledgeItemStatus::getCode, status -> status));
    }

    /**
     * @param value
     * @return
     */
    public static KnowledgeItemStatus of(Integer value){
       return mapping.get(value);
    }
}
