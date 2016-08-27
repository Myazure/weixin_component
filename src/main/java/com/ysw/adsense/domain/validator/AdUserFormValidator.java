package com.ysw.adsense.domain.validator;

import com.ysw.adsense.domain.entity.AdUserEntity;
import com.ysw.adsense.service.AdUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author csyangchsh@gmail.com
 */
@Component
public class AdUserFormValidator implements Validator {

    private final AdUserService userService;

    @Autowired
    public AdUserFormValidator(AdUserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(AdUserEntity.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AdUserEntity form = (AdUserEntity) target;
        validatePasswords(errors, form);
        validateName(errors, form);
    }

    private void validatePasswords(Errors errors, AdUserEntity form) {
        if (!form.getPassword().equals(form.getPasswordRepeated())) {
            errors.rejectValue("password", "password", "Passwords do not match");
        }
    }

    private void validateName(Errors errors, AdUserEntity form) {
        if (userService.getAdUserByName(form.getUserName()) != null) {
            errors.rejectValue("userName", "userName", "User with this name already exists");
        }
    }
}
