package org.myazure.weixin.controller;

import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.myazure.weixin.domain.MaUser;
import org.myazure.weixin.domain.entity.MaUserEntity;
import org.myazure.weixin.domain.validator.AdUserFormValidator;
import org.myazure.weixin.service.MaUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
    private final MaUserService userService;
    private final AdUserFormValidator userCreateFormValidator;
    
    @Autowired
    public UserController(MaUserService userService, AdUserFormValidator userCreateFormValidator) {
        this.userService = userService;
        this.userCreateFormValidator = userCreateFormValidator;
    }

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(userCreateFormValidator);
    }

    @RequestMapping("/users")
    public ModelAndView getUsersPage() {
        LOGGER.debug("[Myazure Weixin]: Getting users page");
        List<MaUser> users = (List<MaUser>) userService.getAllAdUsers();
        return new ModelAndView("users", "users", users);
    }

    @PreAuthorize("@currentUserServiceImpl.canAccessUser(principal, #id)")
    @RequestMapping("/user")
    public ModelAndView getUserPage(@RequestParam(value = "id") Long id) {
        LOGGER.debug("[Myazure Weixin]: Getting user page for user={}", id);
        MaUser user = userService.getAdUserById(id);
        if(user == null) {
            throw new NoSuchElementException(String.format("User=%s not found", id));
        }
        return new ModelAndView("user", "user", user);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/user/create", method = RequestMethod.GET)
    public ModelAndView getUserCreatePage(@RequestParam(value = "id", required = false) Long id) {
        LOGGER.debug("[Myazure Weixin]: Getting user create form");
        MaUserEntity form = new MaUserEntity();
        if(null != id) {
            MaUser user = userService.getAdUserById(id);
            if(null != user) {
            	form.setId(user.getId());
            	form.setUserName(user.getUserName());
            	form.setRole(user.getRole());
            }
        }
        return new ModelAndView("user_create", "form", form);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/user/create", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") MaUserEntity form, BindingResult bindingResult) {
        LOGGER.debug("[Myazure Weixin]: Processing user create form={}, bindingResult={}", form, bindingResult);
        if (bindingResult.hasErrors()) {
            // failed validation
            return "user_create";
        }
        try {
            userService.create(form);
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("[Myazure Weixin]: Exception occurred when trying to save the user, assuming duplicate user name", e);
            bindingResult.rejectValue("userName", "userName", "name already exists");
            return "user_create";
        }
        return "redirect:/users";
    }

}
