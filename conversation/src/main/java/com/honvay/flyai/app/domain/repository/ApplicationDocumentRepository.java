package com.honvay.flyai.app.domain.repository;

import com.honvay.flyai.app.domain.model.Application;
import com.honvay.flyai.document.domain.model.Document;

import java.util.List;

public interface ApplicationDocumentRepository {

    List<Document> findByApplication(Application application);

}
