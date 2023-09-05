package com.honvay.flyai.app.web;

import com.honvay.flyai.app.domain.model.Application;
import org.springframework.stereotype.Component;

@Component
public class ApplicationAssembler {

    public ApplicationVo assemble(Application application){
        ApplicationVo applicationVo = new ApplicationVo();
        applicationVo.setId(application.getId());
        applicationVo.setName(application.getName());
        applicationVo.setLogo(application.getLogo());
        applicationVo.setIntroduction(application.getIntroduction());
        return  applicationVo;
    }

}
