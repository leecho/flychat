package com.honvay.flyai.app.infra.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("application_document")
public class ApplicationDocumentPo {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long applicationId;

    private Long documentId;
}
