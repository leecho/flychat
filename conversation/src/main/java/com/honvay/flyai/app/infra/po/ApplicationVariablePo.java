package com.honvay.flyai.app.infra.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("application_variable")
public class ApplicationVariablePo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String key;

    private Boolean required;

    private String inputType;

    private String options;

}
