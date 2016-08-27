package com.ysw.adsense.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.codec.binary.Base64;
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

import weixin.popular.bean.qrcode.QrcodeTicket;

import com.ysw.adsense.domain.AdEvent;
import com.ysw.adsense.domain.AdEventMat;
import com.ysw.adsense.domain.AdMat;
import com.ysw.adsense.domain.AdQR;
import com.ysw.adsense.domain.entity.AdUserEntity;
import com.ysw.adsense.domain.validator.AdEventFormValidator;
import com.ysw.adsense.service.AdEventService;
import com.ysw.adsense.service.AdQRService;
import com.ysw.adsense.service.AdUserService;
import com.ysw.adsense.service.AdsenseAPI;

@Controller
public class EventController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
    private final AdUserService userService;
    private final AdEventService eventService;
    private final AdEventFormValidator eventCreateFormValidator;

	@Autowired
	private AdQRService qrService;
	
	@Autowired
	private AdsenseAPI adsenseAPI;
	
    @Autowired
    public EventController(
    		AdEventService eventService,
    		AdUserService userService, 
    		AdEventFormValidator eventCreateFormValidator) {
        this.eventService = eventService;
        this.userService = userService;
        this.eventCreateFormValidator = eventCreateFormValidator;
    }

    @InitBinder("form")
    public void initBinder(WebDataBinder binder) {
        binder.addValidators(eventCreateFormValidator);
    }

    @RequestMapping("/events")
    public ModelAndView getEventsPage() {
        LOGGER.debug("[YSW Adsense]: Getting events page");
        return new ModelAndView("events", "events", eventService.fetchAdEventList());
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/event")
    public ModelAndView getUserPage(@RequestParam(value = "id") Long id) {
        LOGGER.debug("[YSW Adsense]: Getting event page for event={}", id);
        AdEvent event = eventService.fetchAdEventById(id);
        if(event == null) {
            throw new NoSuchElementException(String.format("User=%s not found", id));
        }
        List<AdEventMat> eventMats = eventService.findEventMatByEventId(id);
        ModelAndView modelAndView = new ModelAndView();  
        modelAndView.addObject("event", event);
        modelAndView.addObject("eventMats", eventMats);
        modelAndView.setViewName("event"); 
        return modelAndView;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/event/create", method = RequestMethod.GET)
    public ModelAndView getEventCreatePage(@RequestParam(value = "id", required = false) Long id) {
        LOGGER.debug("[YSW Adsense]: Getting event create form");
        AdUserEntity form = new AdUserEntity();
        if(null != id) {
        	AdEvent event = eventService.fetchAdEventById(id);
            if(null != event) {
            	form.setId(event.getId());
            	//form.setUserName(event.getUserName());
            	//form.setRole(event.getRole());
            }
        }
        return new ModelAndView("user_create", "form", form);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping(value = "/event/create", method = RequestMethod.POST)
    public String handleUserCreateForm(@Valid @ModelAttribute("form") AdUserEntity form, BindingResult bindingResult) {
        LOGGER.debug("[YSW Adsense]: Processing event create form={}, bindingResult={}", form, bindingResult);
        if (bindingResult.hasErrors()) {
            // failed validation
            return "user_create";
        }
        try {
            userService.create(form);
        } catch (DataIntegrityViolationException e) {
            LOGGER.warn("Exception occurred when trying to save the event, assuming duplicate event name", e);
            bindingResult.rejectValue("userName", "userName", "name already exists");
            return "user_create";
        }
        // ok, redirect
        return "redirect:/events";
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @RequestMapping("/genqr")
    public void genQRForMat(
    		@RequestParam(value = "eventid") Long eventId, 
    		@RequestParam(value = "matid") int matId,
    		HttpServletResponse response) throws IOException {
        LOGGER.debug("[YSW Adsense]: Gen QR for mat = {}", matId);
		AdMat mat = eventService.findMatByMatId(matId);
		AdEvent event = eventService.fetchAdEventById(eventId);
		String authorizerAppId = event.getOfficialAccount().getAppId();
		String authorizerAccessToken = adsenseAPI.getAuthorizerAccessTokenStr(authorizerAppId);
		String encodedEventId = Base64.encodeBase64URLSafeString(String.valueOf(eventId + "." + mat.getId()).getBytes());
		QrcodeTicket ticket = adsenseAPI.getQRCodeTicketFinalStr(authorizerAccessToken, encodedEventId);
		if(null != ticket && ticket.getErrcode() == null) {
			AdQR qr = qrService.findByQrTicket(ticket.getTicket());
			if(null == qr) {
				qr = new AdQR();
			}
			qr.setEvent(event);
			qr.setQrTicket(ticket.getTicket());
			qr.setQrUrl(ticket.getUrl());
			qr.setMat(mat);
			qrService.create(qr);

			BufferedImage qrImage = adsenseAPI.showQRTicket(ticket.getTicket());
			ImageIO.write(qrImage , "JPEG", response.getOutputStream()); 
		}
    }
}
