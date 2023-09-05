package com.honvay.flyai.document.infra.splitter;

import com.honvay.flyai.document.domain.model.SplitType;
import dev.langchain4j.data.document.Document;

import java.util.List;
import java.util.Map;

public interface TextSplitter {

    List<String> split(Document document, Map<String,Object> properties);

    SplitType getSplitType();

}
