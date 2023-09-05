package com.honvay.flyai.document.infra.po;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("document")
public class DocumentPo {

    private Long id;

    private Integer splitType;

    private Integer splitStep;

    private String name;

    private Long userId;

    private Date createTime;

    private Date updateTime;

}
