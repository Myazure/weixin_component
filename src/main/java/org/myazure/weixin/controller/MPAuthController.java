package org.myazure.weixin.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myazure.weixin.MyazureWeixinAPI;
import org.myazure.weixin.configuration.AppUrlService;
import org.myazure.weixin.domain.CurrentUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author WangZhen
 */
@Controller
public class MPAuthController {
    private static final Logger LOG = LoggerFactory.getLogger(MPAuthController.class);

    @Autowired
    private AppUrlService urlService;

    @Autowired
    private MyazureWeixinAPI myazureWeixinAPI;
    
    //授权体验页面
    @RequestMapping("/test")
    public ModelAndView test(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("test");
    	String link = myazureWeixinAPI.componentLoginPage(urlService.getAppUrl() + "/callback/authorize");
        modelAndView.addObject("link",link);
        return modelAndView;
    }

    //授权链接
    @RequestMapping(value = "/authorize")
    public void goAuthor(@ModelAttribute("currentUser") CurrentUser currentUser,
                         HttpServletRequest request, HttpServletResponse response) throws IOException {
    	if(null == myazureWeixinAPI.getPreAuthCodeStr()) {
        	response.sendRedirect(urlService.getAppUrl() + "/?error='preauthcode'");
        	return;
    	}
    	String link = myazureWeixinAPI.componentLoginPage(urlService.getAppUrl() + "/callback/authorize");
    	response.sendRedirect(link);
    }
}