package com.honvay.flyai.app.infra.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("application")
public class ApplicationPo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String logo;

    private String introduction;

    private Long owner;

    private String modelName;

    private String limitPrompt;

    private Double temperature;

    private Double topP;

    private Integer maxTokens;

    private Double presencePenalty;

    private Double frequencyPenalty;

    private Double similarity;

    private int relevantSize;

}
