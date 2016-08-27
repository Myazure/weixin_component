package com.ysw.adsense.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import weixin.popular.api.MessageAPI;
import weixin.popular.bean.component.ApiGetAuthorizerInfoResult;
import weixin.popular.bean.component.ApiGetAuthorizerInfoResult.Authorizer_info;
import weixin.popular.bean.component.ApiQueryAuthResult;
import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;
import weixin.popular.bean.component.ComponentReceiveXML;
import weixin.popular.bean.component.FuncInfo;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.TextMessage;
import weixin.popular.bean.user.User;

import com.alibaba.fastjson.JSON;
import com.qq.weixin.mp.aes.AesException;
import com.ysw.adsense.configuration.AppUrlService;
import com.ysw.adsense.constant.AdConstants;
import com.ysw.adsense.domain.AdEvent;
import com.ysw.adsense.domain.AdEventHistory;
import com.ysw.adsense.domain.AdMat;
import com.ysw.adsense.domain.AdOfficialAccount;
import com.ysw.adsense.domain.AdOfficialAccountWxUser;
import com.ysw.adsense.domain.AdQR;
import com.ysw.adsense.domain.AdUser;
import com.ysw.adsense.domain.AdWxUser;
import com.ysw.adsense.domain.CurrentUser;
import com.ysw.adsense.ip.IPManagementClient;
import com.ysw.adsense.ip.request.GoodsReq;
import com.ysw.adsense.ip.request.LockItem;
import com.ysw.adsense.ip.request.LockReq;
import com.ysw.adsense.ip.request.OrderItem;
import com.ysw.adsense.ip.request.OrderReq;
import com.ysw.adsense.ip.response.SingleGoodsResponse;
import com.ysw.adsense.ip.response.StatusResponse;
import com.ysw.adsense.service.AdEventService;
import com.ysw.adsense.service.AdOfficialAccountService;
import com.ysw.adsense.service.AdQRService;
import com.ysw.adsense.service.AdUserService;
import com.ysw.adsense.service.AdsenseAPI;
import com.ysw.adsense.service.BASE;
import com.ysw.adsense.utils.LocationUtils;

/**
 * @author csyangchsh@gmail.com Date: 15-12-18
 * @author WangZhen <wangzhenjjcn@gmail.com> Date 16-06-20
 */
@Controller
public class MPController {

	private static final Logger LOG = LoggerFactory.getLogger(MPController.class);

	@Autowired
	private AppUrlService urlService;

	@Autowired
	private AdOfficialAccountService officialAccountService;

	@Autowired
	private AdEventService eventService;
	@Autowired
	private AdUserService userService;

	@Autowired
	private AdQRService qrService;

	@Autowired
	private AdsenseAPI adsenseAPI;

	@Autowired
	private IPManagementClient ipManagementClient;

	@Autowired
	private StringRedisTemplate redisTemplate;

	@RequestMapping(path = "/debug", method = RequestMethod.GET)
	public String debug() {
		return "redirect:/";
	}

	/**
	 * 授权事件处理 1.授权成功 2.取消授权 3.更新授权 4.凭证发放
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws AesException
	 */
	@RequestMapping(path = "/event/authorize", method = RequestMethod.POST)
	public void acceptAuthorizeEvent(HttpServletRequest request, HttpServletResponse response) throws IOException, AesException {
		ComponentReceiveXML eventMessage = adsenseAPI.getEventMessage(request, response, ComponentReceiveXML.class);
		if (eventMessage == null) {
			outputStreamWrite(response.getOutputStream(), "success");
			return;
		}
		LOG.debug(BASE.LOG_SPLIT_LINE);
		LOG.debug(eventMessage.getInfoType());
		switch (eventMessage.getInfoType()) {
		case "component_verify_ticket":
			adsenseAPI.refresh(eventMessage.getComponentVerifyTicket());
			break;
		case "unauthorized":
			LOG.debug(BASE.LOG_SPLIT_LINE);
			LOG.debug("[YSW Adsense]: unauthorized:" + eventMessage.getAppId());
			AdOfficialAccount oaUnauthorized = officialAccountService.findByAppId(eventMessage.getAuthorizerAppid());
			if (oaUnauthorized != null) {
				oaUnauthorized.setAuthorized(false);
				oaUnauthorized.setRefreshToken(null);
				officialAccountService.updateAdOfficialAccount(oaUnauthorized);
			} else {
				LOG.error("Null Piont Exception ~! At appID not Founded:" + eventMessage.getAppId());
			}
			break;
		case "updateauthorized":
			LOG.debug(BASE.LOG_SPLIT_LINE);
			LOG.debug("[YSW Adsense]: updateauthorized:" + eventMessage.getAppId());
			AdOfficialAccount oaUpdate = officialAccountService.findByAppId(eventMessage.getAuthorizerAppid());
			if (oaUpdate == null) {
				oaUpdate = new AdOfficialAccount();
				oaUpdate.setAppId(eventMessage.getAuthorizerAppid());
			}
			oaUpdate.setAuthorized(eventMessage.getAuthorizationCode() != null ? true : false);
			oaUpdate.setRefreshToken(adsenseAPI.getAuthInfo(eventMessage.getAuthorizationCode()).getAuthorization_info().getAuthorizer_refresh_token());
			checkAuthorize(adsenseAPI.getAuthInfo(eventMessage.getAuthorizationCode()).getAuthorization_info().getFunc_info());
			break;
		case "authorized":
			LOG.debug(BASE.LOG_SPLIT_LINE);
			LOG.debug("[YSW Adsense]: authorized:" + eventMessage.getAppId());
			AdOfficialAccount oaAuthorized = officialAccountService.findByAppId(eventMessage.getAuthorizerAppid());

			if (oaAuthorized == null) {
				oaAuthorized = new AdOfficialAccount();
				oaAuthorized.setAppId(eventMessage.getAuthorizerAppid());
			}
			oaAuthorized.setAuthorized(eventMessage.getAuthorizationCode() != null ? true : false);
			oaAuthorized.setRefreshToken(adsenseAPI.getAuthInfo(eventMessage.getAuthorizationCode()).getAuthorization_info().getAuthorizer_refresh_token());
			checkAuthorize(adsenseAPI.getAuthInfo(eventMessage.getAuthorizationCode()).getAuthorization_info().getFunc_info());
			break;
		default:
			LOG.debug(BASE.LOG_SPLIT_LINE);
			LOG.error("[YSW Adsense]: NOT FOUNDED ! Event Type: ", eventMessage.getInfoType());
			break;
		}
		outputStreamWrite(response.getOutputStream(), "success");
	}

	/**
	 * 授权回掉接口 1.取得当前操作账户 2.授权校验 3.加入数据库存档刷新令牌
	 * 
	 * @param session
	 * @param currentUser
	 * @param authCode
	 * @param expires
	 * @return
	 */
	@RequestMapping(path = "/callback/authorize", method = RequestMethod.GET)
	public String authorCallback(HttpSession session, @ModelAttribute("currentUser") CurrentUser currentUser,
			@RequestParam(value = "auth_code", required = true) String authCode, @RequestParam(value = "expires_in", required = true) Long expires) {
		Long userId = currentUser.getId();
		AdUser user = userService.getAdUserById(userId);
		ApiQueryAuthResult authInfoRes = adsenseAPI.getAuthInfo(authCode);
		Authorization_info authInfo = authInfoRes.getAuthorization_info();
		ApiGetAuthorizerInfoResult userInfo = adsenseAPI.getAuthUserInfo(authInfo.getAuthorizer_appid());
		Authorizer_info oaAccount = userInfo.getAuthorizer_info();
		AdOfficialAccount oaUpdate = officialAccountService.findByAppId(authInfo.getAuthorizer_appid());
		if (oaUpdate == null) {
			oaUpdate = new AdOfficialAccount();
			oaUpdate.setAppId(authInfoRes.getAuthorization_info().getAuthorizer_appid());
		}
		oaUpdate.setAuthorized(authInfoRes.isSuccess());
		oaUpdate.setHeadImgUrl(oaAccount.getHead_img());
		oaUpdate.setNickName(oaAccount.getNick_name());
		oaUpdate.setRefreshToken(authInfo.getAuthorizer_refresh_token());
		oaUpdate.setUser(user);
		oaUpdate.setUserName(oaAccount.getUser_name());
		officialAccountService.updateAdOfficialAccount(oaUpdate);
		return "redirect:/";
	}

	/**
	 * 接受腾讯的回调信息
	 * 
	 * @author WangZhen
	 * @since branch wang
	 * @param appId
	 *            授权应用的AppId
	 * @param request
	 *            回调请求
	 * @param response
	 *            回调响应
	 * @throws IOException
	 *             异常
	 */
	@RequestMapping(path = "/callback/{appId}/callback", method = RequestMethod.POST)
	public void acceptMessageAndEvent(@PathVariable("appId") String appId, HttpServletRequest request, HttpServletResponse response) throws Exception {
		// get event message info
		EventMessage eventMessage = adsenseAPI.getEventMessage(request, response, EventMessage.class);
		// response
		// Event Message is null
		if (eventMessage == null) {
			LOG.error("[YSW Adsense]: NOT FOUNDED Event Message");
			return;
		}
		// Find Official Account in db by event message
		AdOfficialAccount oaAccount = officialAccountService.findByAppId(appId);
		if (oaAccount == null) {
			// Official Account not found do nothing
			LOG.error("[YSW Adsense]: NOT FOUNDED Official Account: {}", appId);
			return;
		}
		try {
			this.dispatchMessage(eventMessage);
		} catch (Exception e) {
			LOG.debug(e.toString());
			e.printStackTrace();
			return;
		}
		return;
	}

	private boolean distributeGoods(AdEvent event, AdMat mat, AdWxUser wxUser, AdOfficialAccountWxUser oaWxUser) {
		// Communicate with IP Server here!!!
		LockItem item = new LockItem(event.getGoods().getYswGoodsId(), 1);
		List<LockItem> lockItemList = new ArrayList<LockItem>(1);
		lockItemList.add(item);
		OrderItem orderItem = new OrderItem();
		orderItem.setGoods(lockItemList);
		orderItem.setOrder_price(BigDecimal.ZERO);
		orderItem.setOrder_date(new Date());
		// Add order id
		orderItem.setOrder_id("WX_" + UUID.randomUUID());
		OrderReq orderReq = new OrderReq();
		List<OrderItem> orderItems = new ArrayList<OrderItem>(1);
		orderItems.add(orderItem);
		orderReq.setOrder_info(orderItems);
		orderReq.setCustomer_id(0L);
		orderReq.setMat_id(mat.getMatCode());
		GoodsReq goodsReq = new GoodsReq(mat.getMatCode(), event.getGoods().getYswGoodsId());
		SingleGoodsResponse goodsInventoryRes = null;
		goodsInventoryRes = ipManagementClient.getGoodsOfMat(goodsReq);
		LOG.debug(goodsInventoryRes.getMessage());
		if (goodsInventoryRes.isSuccess()) {
			if (goodsInventoryRes.getData().size() > 0) {
				if (goodsInventoryRes.getData().get(0).getNumber()>0) {
					int stroge = goodsInventoryRes.getData().get(0).getNumber();
					LockReq lockReq = new LockReq();
					lockReq.setGoods(lockItemList);
					lockReq.setMatId(mat.getMatCode());
					lockReq.setType(LockReq.LOCK);
					StatusResponse lockRes = ipManagementClient.getLockGoods(lockReq);
					LOG.debug(lockRes.getMessage());
					if (lockRes != null && lockRes.isSuccess()) {
						TextMessage msg = new TextMessage(oaWxUser.getOpenId(), lockRes.getMessage() + "\n您的礼品已准备完毕，正在出货中！");
						MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(event.getOfficialAccount().getAppId()), msg);
						StatusResponse goodsOrderRes = ipManagementClient.customerOrder(orderReq);
						boolean distributed = false;
						LOG.debug(goodsOrderRes.getMessage());
						if (goodsOrderRes != null && goodsOrderRes.isSuccess()) {
							if (goodsOrderRes.getMessage().contains("错误")) {
								distributed = false;
							} else {
								distributed = true;
							}
						} else {
							goodsInventoryRes = ipManagementClient.getGoodsOfMat(goodsReq);
							LOG.debug(goodsInventoryRes.getMessage());
							if (goodsInventoryRes.isSuccess() && goodsInventoryRes != null) {
								if (goodsInventoryRes.getData().get(0).getNumber() > 0) {
									if (goodsInventoryRes.getData().get(0).getNumber() - stroge == 1) {
										distributed = true;
									} else if (goodsInventoryRes.getData().get(0).getNumber() == stroge) {
										
									} else {
										distributed = true;
									}
								} else {
									distributed = true;
								}
							}
						}
						if (distributed) {
							// 发送成功
							AdEventHistory newHistory = new AdEventHistory();
							newHistory.setAction(AdConstants.EVENT_HISTORY_TYPE_DISTRIBUTED);
							newHistory.setEvent(event);
							newHistory.setWxUser(wxUser);
							newHistory.setDetails("Distributed goods...");
							newHistory.setIpOrderNo(goodsOrderRes.getCode());
							eventService.createEventHistory(newHistory);
							TextMessage sucessMsg = new TextMessage(oaWxUser.getOpenId(), "\n您的礼品已发放，请在取货口取货！");
							MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(event.getOfficialAccount().getAppId()), sucessMsg);
							return true;
						} else {
							// 设备故障消息
							TextMessage unsucessMsg = new TextMessage(oaWxUser.getOpenId(), "\n您的礼品由于设备故障发放失败，抱歉！");
							MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(event.getOfficialAccount().getAppId()), unsucessMsg);
							return false;
						}
					} else {
						// 锁定失败，说明用户操作了
						TextMessage msg = new TextMessage(oaWxUser.getOpenId(), "\n感谢您参加【药尚网】" + event.getName() + "的活动！\n请确认您已在设备面前，\n并请您将设备退回首页重新扫描二维码！");
						MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(event.getOfficialAccount().getAppId()), msg);
						return false;
					}
				}else {
					// 库存不足msg
					TextMessage msg = new TextMessage(oaWxUser.getOpenId(), "感谢您参加【药尚网】" + event.getName() + "的活动！\n今天的礼品已派完请改日再来！");
					MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(event.getOfficialAccount().getAppId()), msg);
					return false;	
				}
			} else {
				// 库存不足msg
				TextMessage msg = new TextMessage(oaWxUser.getOpenId(), "感谢您参加【药尚网】" + event.getName() + "的活动！\n今天的礼品已派完请改日再来！");
				MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(event.getOfficialAccount().getAppId()), msg);
				return false;
			}
		} else {
			// 售药机offline
			TextMessage msg = new TextMessage(oaWxUser.getOpenId(), "感谢您参加【药尚网】" + event.getName() + "的活动！\n当前设备通讯故障，请稍候再试！");
			MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(event.getOfficialAccount().getAppId()), msg);
			return false;
		}
	}

	/**
	 * 消息事件分发
	 */
	private long dispatchMessage(EventMessage eventMessage) {
		LOG.info("[YSW Adsense]: Distribute message start");
		if (eventMessage.getMsgType() == null || eventMessage.getMsgType().equals("")) {
			LOG.error("[YSW Adsense]: msgType is null");
		}
		LOG.info("[YSW Adsense]: eventMessage is " + JSON.toJSONString(eventMessage));
		String msgTypeStr = eventMessage.getMsgType();
		LOG.info("[YSW Adsense]: msgType is " + msgTypeStr);

		long eventHistoryId = 0l;
		switch (msgTypeStr) {
		case "event":
			eventHistoryId = dispatchEvent(eventMessage);
			break;
		default:
			LOG.info("[YSW Adsense]: Dispatch event, event is {}", eventMessage.getEvent());
			break;
		}
		return eventHistoryId;
	}

	private boolean checkAuthorize(List<FuncInfo> func_info) {
		return false;
	}

	/**
	 * event事件分发
	 * 
	 * @param eventMessage
	 */
	private long dispatchEvent(EventMessage eventMessage) {
		String event = eventMessage.getEvent();
		String eventKey = eventMessage.getEventKey();
		LOG.info("[YSW Adsense]: Dispatch event, event is {}", event);
		// Fetch wx user and store it
		String openId = eventMessage.getFromUserName();
		String oaUserName = eventMessage.getToUserName();
		AdOfficialAccount oa = officialAccountService.findByUserName(oaUserName);
		String authorizerAccessToken = adsenseAPI.getAuthorizerAccessTokenStr(oa.getAppId());
		// Check if the relationship between Official Account and WxUser exists
		AdOfficialAccountWxUser oaWxUser = eventService.findOAWxUserByOAIdAndWxUserOpenId(oa.getId(), openId);
		// Fetch user info from WX

		// : limited time get from api,check first at db then api
		User user = adsenseAPI.getUserInfo(authorizerAccessToken, openId);
		String qrScene = "";
		String scenceStr = "";
		String[] paramsStr = null;
		Long eventId = null;
		Long matId = null;
		AdEvent adEvent = null;
		AdMat adMat = null;
		switch (event) {
		case "subscribe":
			if (null == eventKey || eventKey.trim().length() == 0) {
				return 0l;
			}
			String prefix = "qrscene_";
			int qrSceneIndex = eventKey.indexOf(prefix);
			if (qrSceneIndex != 0) {
				return 0l;
			}
			// Validate event
			qrScene = eventKey.substring(prefix.length(), eventKey.length());
			scenceStr = new String(Base64.decodeBase64(qrScene));
			paramsStr = scenceStr.split("\\.");
			if (paramsStr.length != 2) {
				return 0l;
			}
			eventId = Long.parseLong(paramsStr[0]);
			matId = Long.parseLong(paramsStr[1]);
			adEvent = eventService.fetchAdEventById(eventId);
			adMat = eventService.findMatByMatId(matId);
			// No event for this Official Account
			if (null == adEvent) {
				LOG.info("[YSW Adsense]: No found Event for Official Account {}...", eventMessage.getToUserName());
				return 0l;
			}
			if (adEvent.getEventType() == AdConstants.EVENT_TYPE_SCAN) {
				processScanDistribution(eventMessage, adEvent, adMat, oaWxUser, user);
			}
			break;
		case "SCAN":
			if (null == eventKey || eventKey.trim().length() == 0) {
				return 0l;
			}
			scenceStr = new String(Base64.decodeBase64(eventKey));
			paramsStr = scenceStr.split("\\.");
			if (paramsStr.length != 2) {
				return 0l;
			}
			eventId = Long.parseLong(paramsStr[0]);
			matId = Long.parseLong(paramsStr[1]);
			adEvent = eventService.fetchAdEventById(eventId);
			adMat = eventService.findMatByMatId(matId);
			// No event for this Official Account

			if (null == adEvent) {
				LOG.info("[YSW Adsense]: No found Event for Official Account {}...", eventMessage.getToUserName() + "  event ID：" + eventId + "   matID:"
						+ matId);
				LOG.info("[YSW Adsense]:----adEvent:" + JSON.toJSONString(adEvent));
				return 0l;
			}
			if (adEvent.getEventType() == AdConstants.EVENT_TYPE_SCAN) {
				processScanDistribution(eventMessage, adEvent, adMat, oaWxUser, user);
			}
			break;
		case "unsubscribe":
			unsubscribe(eventMessage, oaWxUser);
			break;
		case "LOCATION":
			// For scan event
			processScanDistributionByLocation(eventMessage, oaWxUser);
			break;
		default:
			break;
		}
		return 0l;
	}

	private long processScanDistribution(EventMessage eventMessage, AdEvent adEvent, AdMat adMat, AdOfficialAccountWxUser oaWxUser, User user) {
		boolean hasDistributed = Boolean.FALSE;
		AdWxUser wxUser = null;
		if (null == oaWxUser) {
			// Save wxUser
			wxUser = this.createAdWxUser(user);
			// Save Official Account wxUser relationship
			oaWxUser = this.createAdOfficialAccountWxUser(user, adEvent.getOfficialAccount(), wxUser);
		} else {
			wxUser = oaWxUser.getWxUser();

			oaWxUser.setSubscribed(Boolean.TRUE);
			// oaWxUser.setSubscribedTime(new Date());
			eventService.saveOAWxUser(oaWxUser);

			// Check if distribute goods
			AdEventHistory distributedHistory = eventService.findEventHistoryByEventIdAndWxUserId(adEvent.getId(), wxUser.getId(),
					AdConstants.EVENT_HISTORY_TYPE_DISTRIBUTED);
			if (null != distributedHistory) {
				hasDistributed = Boolean.TRUE;
			}
		}
		// Save scan event
		AdEventHistory history = this.saveEventHistory(eventMessage, adEvent, oaWxUser.getWxUser());
		AdEventHistory distributingHistory = null;
		// Has distributed gift to this wxUser already
		if (hasDistributed) {
			// First return a message
			TextMessage msg = new TextMessage(eventMessage.getFromUserName(), "您已参加此活动并领取礼品，感谢您的参与！");
			MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(adEvent.getOfficialAccount().getAppId()), msg);
			return 0l;
		}
		// No, trying to distributing gift
		else {
			// Store to db
			distributingHistory = new AdEventHistory();
			distributingHistory.setAction(AdConstants.EVENT_HISTORY_TYPE_DISTRIBUTING);
			distributingHistory.setEvent(adEvent);
			distributingHistory.setWxUser(wxUser);
			distributingHistory.setDetails("Distributing goods...");
			eventService.createEventHistory(distributingHistory);
			// Store to redis
			distributingHistory.setAdMat(adMat);
			String key = eventMessage.getFromUserName() + "." + eventMessage.getToUserName();
			redisTemplate.opsForSet().pop(key);
			redisTemplate.opsForSet().add(key, JSON.toJSONString(distributingHistory));
			redisTemplate.expire(key, 10, TimeUnit.MINUTES);
		}
		// First return a message
		TextMessage msg = new TextMessage(eventMessage.getFromUserName(), "感谢您参加【药尚网】" + adEvent.getName() + "的活动！此活动需要您共享位置哦！");
		MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(adEvent.getOfficialAccount().getAppId()), msg);
		return hasDistributed ? 0l : history.getId();
	}

	private void unsubscribe(EventMessage eventMessage, AdOfficialAccountWxUser oaWxUser) {
		LOG.info("[YSW Adsense]: Unsubscribe envet, only exist OA Wxuser will be updated.");
		if (null != oaWxUser) {
			oaWxUser.setSubscribed(Boolean.FALSE);
			oaWxUser.setSubscribedTime(new Date());
			eventService.saveOAWxUser(oaWxUser);
			this.saveEventHistory(eventMessage, null, oaWxUser.getWxUser());
		}
	}

	private long processScanDistributionByLocation(EventMessage eventMessage, AdOfficialAccountWxUser oaWxUser) {
		LOG.info("[YSW Adsense]: Dispatch event, event is {}", eventMessage.getEvent());
		if (eventMessage.getLatitude() == null || eventMessage.getLongitude() == null) {
			return 0l;
		}
		String key = eventMessage.getFromUserName() + "." + eventMessage.getToUserName();
		String distributingHistoryStr = redisTemplate.opsForSet().pop(key);
		if (distributingHistoryStr == null || distributingHistoryStr.length() == 0) {
			return 0l;
		}

		AdEventHistory distributingHistory = JSON.parseObject(distributingHistoryStr, AdEventHistory.class);
		// redisTemplate.opsForSet().remove(key);

		AdMat adMat = distributingHistory.getAdMat();
		AdEvent adEvent = distributingHistory.getEvent();
		AdWxUser wxUser = distributingHistory.getWxUser();
		if (LocationUtils.inRange(eventMessage.getLatitude(), eventMessage.getLongitude(), adMat.getLocationX(), adMat.getLocationY(),
				adEvent.getSafeDistance())) {
			LOG.debug("Adsense: User distence========="
					+ LocationUtils.distance(eventMessage.getLatitude(), eventMessage.getLongitude(), adMat.getLocationX(), adMat.getLocationY()));
			boolean distributeSuccess = this.distributeGoods(adEvent, adMat, wxUser, oaWxUser);
			if (!distributeSuccess) {
				TextMessage msg = new TextMessage(eventMessage.getFromUserName(), "派发失败，请联系设备管理员！或者稍后再试~");
				MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(adEvent.getOfficialAccount().getAppId()), msg);
			}
		} else {
			// warning location cannot always return this msg
			TextMessage msg = new TextMessage(eventMessage.getFromUserName(),
					"抱歉！您不符合本次礼品派送的规则！\n详细规则请访问<a href=\"http://www.yao-shang-wang.com\">药尚网官方活动网站</a>!\n仍然感谢您的参与");
			MessageAPI.messageCustomSend(adsenseAPI.getAuthorizerAccessTokenStr(adEvent.getOfficialAccount().getAppId()), msg);
		}

		if (null != oaWxUser) {
			this.saveEventHistory(eventMessage, null, oaWxUser.getWxUser());
		}
		return 0l;
	}

	private AdEventHistory saveEventHistory(EventMessage eventMessage, AdEvent event, AdWxUser wxUser) {

		// Popular Event History
		AdEventHistory history = new AdEventHistory();
		history.setAction(eventMessage.getEvent());
		// If related to event
		if (null != event) {
			history.setEvent(event);
		}
		// Find QR
		AdQR qr = qrService.findByQrTicket(eventMessage.getTicket());
		history.setQr(qr);
		history.setWxUser(wxUser);
		history.setDetails(JSON.toJSONString(eventMessage));
		eventService.createEventHistory(history);
		return history;
	}

	private AdWxUser createAdWxUser(User user) {
		AdWxUser wxUser = eventService.findAdWxUserByUnionId(user.getUnionid());
		if (null == wxUser) {
			wxUser = new AdWxUser();
		}
		wxUser.setCity(user.getCity());
		wxUser.setCountry(user.getCountry());
		wxUser.setHeadImgUrl(user.getHeadimgurl());
		wxUser.setLanguage(user.getLanguage());
		wxUser.setNickname(user.getNickname());
		wxUser.setProvince(user.getProvince());
		wxUser.setSex(user.getSex());
		wxUser.setUnionId(user.getUnionid());
		// Save WxUser
		eventService.createWxUser(wxUser);
		return wxUser;
	}

	private AdOfficialAccountWxUser createAdOfficialAccountWxUser(User user, AdOfficialAccount oa, AdWxUser wxUser) {
		AdOfficialAccountWxUser oaWxUser = new AdOfficialAccountWxUser();
		oaWxUser.setOpenId(user.getOpenid());
		oaWxUser.setGroupid(user.getGroupid());
		oaWxUser.setOfficialAccount(oa);
		oaWxUser.setRemark(user.getRemark());
		oaWxUser.setSubscribed(Boolean.TRUE);
		oaWxUser.setSubscribedTime(new Date(user.getSubscribe_time()));
		oaWxUser.setTagidList(null);
		oaWxUser.setWxUser(wxUser);
		eventService.saveOAWxUser(oaWxUser);
		return oaWxUser;
	}

	private boolean outputStreamWrite(OutputStream outputStream, String text) {
		try {
			outputStream.write(text.getBytes("utf-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
}