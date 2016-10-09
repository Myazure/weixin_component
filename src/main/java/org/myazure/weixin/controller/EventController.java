package org.myazure.weixin.controller;

/**
 * 
 * @author WangZhen
 *
 */
public class EventController {

//    private final AdUserService userService;
//    private final AdEventService eventService;
//    private final AdEventFormValidator eventCreateFormValidator;
//
//	@Autowired
//	private AdQRService qrService;
//	
//	@Autowired
//	private AdsenseAPI adsenseAPI;
//	
//    @Autowired
//    public EventController(
//    		AdEventService eventService,
//    		AdUserService userService, 
//    		AdEventFormValidator eventCreateFormValidator) {
//        this.eventService = eventService;
//        this.userService = userService;
//        this.eventCreateFormValidator = eventCreateFormValidator;
//    }
//
//    @InitBinder("form")
//    public void initBinder(WebDataBinder binder) {
//        binder.addValidators(eventCreateFormValidator);
//    }
//
//    @RequestMapping("/events")
//    public ModelAndView getEventsPage() {
//        LOGGER.debug("[Myazure Weixin]: Getting events page");
//        return new ModelAndView("events", "events", eventService.fetchAdEventList());
//    }
//
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @RequestMapping("/event")
//    public ModelAndView getUserPage(@RequestParam(value = "id") Long id) {
//        LOGGER.debug("[Myazure Weixin]: Getting event page for event={}", id);
//        AdEvent event = eventService.fetchAdEventById(id);
//        if(event == null) {
//            throw new NoSuchElementException(String.format("User=%s not found", id));
//        }
//        List<AdEventMat> eventMats = eventService.findEventMatByEventId(id);
//        ModelAndView modelAndView = new ModelAndView();  
//        modelAndView.addObject("event", event);
//        modelAndView.addObject("eventMats", eventMats);
//        modelAndView.setViewName("event"); 
//        return modelAndView;
//    }
//
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @RequestMapping(value = "/event/create", method = RequestMethod.GET)
//    public ModelAndView getEventCreatePage(@RequestParam(value = "id", required = false) Long id) {
//        LOGGER.debug("[Myazure Weixin]: Getting event create form");
//        AdUserEntity form = new AdUserEntity();
//        if(null != id) {
//        	AdEvent event = eventService.fetchAdEventById(id);
//            if(null != event) {
//            	form.setId(event.getId());
//            	//form.setUserName(event.getUserName());
//            	//form.setRole(event.getRole());
//            }
//        }
//        return new ModelAndView("user_create", "form", form);
//    }
//
//    @PreAuthorize("hasAuthority('ADMIN')")
//    @RequestMapping(value = "/event/create", method = RequestMethod.POST)
//    public String handleUserCreateForm(@Valid @ModelAttribute("form") AdUserEntity form, BindingResult bindingResult) {
//        LOGGER.debug("[Myazure Weixin]: Processing event create form={}, bindingResult={}", form, bindingResult);
//        if (bindingResult.hasErrors()) {
//            // failed validation
//            return "user_create";
//        }
//        try {
//            userService.create(form);
//        } catch (DataIntegrityViolationException e) {
//            LOGGER.warn("Exception occurred when trying to save the event, assuming duplicate event name", e);
//            bindingResult.rejectValue("userName", "userName", "name already exists");
//            return "user_create";
//        }
//        // ok, redirect
//        return "redirect:/events";
//    }

//    @PreAuthorize("hasAuthority('ADMIN')")
//    @RequestMapping("/genqr")
//    public void genQRForMat(
//    		@RequestParam(value = "eventid") Long eventId, 
//    		@RequestParam(value = "matid") int matId,
//    		HttpServletResponse response) throws IOException {
//        LOGGER.debug("[Myazure Weixin]: Gen QR for mat = {}", matId);
//		AdMat mat = eventService.findMatByMatId(matId);
//		AdEvent event = eventService.fetchAdEventById(eventId);
//		String authorizerAppId = event.getOfficialAccount().getAppId();
//		String authorizerAccessToken = adsenseAPI.getAuthorizerAccessTokenStr(authorizerAppId);
//		String encodedEventId = Base64.encodeBase64URLSafeString(String.valueOf(eventId + "." + mat.getId()).getBytes());
//		QrcodeTicket ticket = adsenseAPI.getQRCodeTicketFinalStr(authorizerAccessToken, encodedEventId);
//		if(null != ticket && ticket.getErrcode() == null) {
//			AdQR qr = qrService.findByQrTicket(ticket.getTicket());
//			if(null == qr) {
//				qr = new AdQR();
//			}
//			qr.setEvent(event);
//			qr.setQrTicket(ticket.getTicket());
//			qr.setQrUrl(ticket.getUrl());
//			qr.setMat(mat);
//			qrService.create(qr);
//
//			BufferedImage qrImage = adsenseAPI.showQRTicket(ticket.getTicket());
//			ImageIO.write(qrImage , "JPEG", response.getOutputStream()); 
//		}
//    }
}
