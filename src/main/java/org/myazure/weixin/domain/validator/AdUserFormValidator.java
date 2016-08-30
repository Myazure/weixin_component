package org.myazure.weixin.domain.validator;

import org.myazure.weixin.domain.entity.MaUserEntity;
import org.myazure.weixin.service.AdUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * @author WangZhen
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
        return clazz.equals(MaUserEntity.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        MaUserEntity form = (MaUserEntity) target;
        validatePasswords(errors, form);
        validateName(errors, form);
    }

    private void validatePasswords(Errors errors, MaUserEntity form) {
        if (!form.getPassword().equals(form.getPasswordRepeated())) {
            errors.rejectValue("password", "password", "Passwords do not match");
        }
    }

    private void validateName(Errors errors, MaUserEntity form) {
        if (userService.getAdUserByName(form.getUserName()) != null) {
            errors.rejectValue("userName", "userName", "User with this name already exists");
        }
    }
}
