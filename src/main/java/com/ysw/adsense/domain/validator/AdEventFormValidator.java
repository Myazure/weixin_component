package com.ysw.adsense.domain.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ysw.adsense.domain.entity.AdEventEntity;
import com.ysw.adsense.domain.entity.AdUserEntity;
import com.ysw.adsense.service.AdEventService;


@Component
public class AdEventFormValidator implements Validator {

    private final AdEventService eventService;

    @Autowired
    public AdEventFormValidator(AdEventService eventService) {
        this.eventService = eventService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(AdUserEntity.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AdEventEntity form = (AdEventEntity) target;
    }
}
