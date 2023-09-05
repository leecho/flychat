package com.honvay.flyai.document.domain.model;

import com.honvay.flyai.framework.core.enums.GeneralEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum DocumentSegmentStatus implements GeneralEnum<Integer> {
    PENDING(1,"PENDING"),
    DONE(2,"DONE");


    @Getter
    private final Integer code;

    @Getter
    private final String name;

    DocumentSegmentStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }

    private static final Map<Integer, DocumentSegmentStatus> mapping;

    static {
        mapping = Arrays.stream(DocumentSegmentStatus.values())
                .collect(Collectors.toMap(DocumentSegmentStatus::getCode, status -> status));
    }

    /**
     */
    public static DocumentSegmentStatus of(Integer value){
       return mapping.get(value);
    }
}
