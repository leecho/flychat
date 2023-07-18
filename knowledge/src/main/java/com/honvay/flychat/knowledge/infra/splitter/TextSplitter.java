package com.honvay.flychat.knowledge.infra.splitter;

import com.honvay.flychat.knowledge.domain.model.SplitType;
import dev.langchain4j.data.document.Document;

import java.util.List;
import java.util.Map;

public interface TextSplitter {

    List<String> split(Document document, Map<String,Object> properties);

    SplitType getSplitType();

}
