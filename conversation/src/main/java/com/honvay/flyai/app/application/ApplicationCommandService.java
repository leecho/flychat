package com.honvay.flyai.app.application;

import com.honvay.flyai.app.domain.model.Application;

public interface ApplicationCommandService {
    void create(Application application);

    void delete(Application application);
}
