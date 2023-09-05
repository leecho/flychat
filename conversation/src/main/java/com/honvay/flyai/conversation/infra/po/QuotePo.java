package com.honvay.flyai.conversation.infra.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("quote")
public class QuotePo {

    private Long id;

    private Long messageId;

    private Double similarity;

    private String document;

    private String segment;

}
