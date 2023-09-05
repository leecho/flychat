package com.honvay.flyai.app.application;

import com.honvay.flyai.app.domain.model.Application;

import java.util.List;

public interface ApplicationQueryService {
    List<Application> find();
}
