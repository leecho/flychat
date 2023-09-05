package com.honvay.flyai.document.domain.model;

import com.honvay.flyai.framework.core.enums.GeneralEnum;
import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum SplitType implements GeneralEnum<Integer> {
    SENTENCE(1,"SENTENCE"),
    FIXED(2,"FIXED"),
    PARAGRAPH(3,"PARAGRAPH"),
    AI(4,"AI"),
    NONE(5,"NONE");


    @Getter
    private final Integer code;

    @Getter
    private final String name;

    SplitType(Integer value, String code) {
        this.code = value;
        this.name = code;
    }

    private static final Map<Integer,SplitType> mapping;

    static {
        mapping = Arrays.stream(SplitType.values())
                .collect(Collectors.toMap(SplitType::getCode, splitType -> splitType));
    }

    /**
     * @param value
     * @return
     */
    public static SplitType of(Integer value){
        return mapping.get(value);
    }
}
