package com.ysw.adsense.controller;

import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;


@ControllerAdvice
public class ExceptionHandlerControllerAdvice {

    private static Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerControllerAdvice.class);

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNoSuchElementException(NoSuchElementException e) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("error", e.getMessage());
        mav.addObject("status", 404);
        mav.setViewName("error");
        return mav;
    }

//    @ExceptionHandler(WXException.class)
//    @ResponseStatus(HttpStatus.NOT_FOUND)
//    public ModelAndView handleWXException(WXException e) {
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("error", e.getMessage());
//        mav.addObject("status", 500);
//        mav.setViewName("error");
//        return mav;
//    }

}
