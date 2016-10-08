package org.myazure.weixin.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.myazure.weixin.MyazureWeixinAPI;
import org.myazure.weixin.configuration.AppUrlService;
import org.myazure.weixin.constant.MyazureConstants;
import org.myazure.weixin.domain.MaOfficialAccount;
import org.myazure.weixin.domain.MaUser;
import org.myazure.weixin.domain.CurrentUser;
import org.myazure.weixin.handlers.AuthorizeHandler;
import org.myazure.weixin.processor.MessagePostman;
import org.myazure.weixin.service.MaOfficialAccountService;
import org.myazure.weixin.service.MaUserService;
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

import weixin.popular.api.TokenAPI;
import weixin.popular.bean.component.ApiGetAuthorizerInfoResult;
import weixin.popular.bean.component.ApiGetAuthorizerInfoResult.Authorizer_info;
import weixin.popular.bean.component.ApiQueryAuthResult;
import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;
import weixin.popular.bean.component.ComponentReceiveXML;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.message.message.TextMessage;

import com.alibaba.fastjson.JSON;
import com.qq.weixin.mp.aes.AesException;

/**
 * @author WangZhen Date: 15-12-18
 * @author WangZhen <wangzhenjjcn@gmail.com> Date 16-06-20
 */
@Controller
public class MPController {

	private static final Logger LOG = LoggerFactory.getLogger(MPController.class);

	@Autowired
	private AppUrlService urlService;
	@Autowired
	private MaUserService userService;
	@Autowired
	private MyazureWeixinAPI myazureWeixinAPI;
	@Autowired
	private MaOfficialAccountService maOfficialAccountService;
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Autowired
	private AuthorizeHandler authorizeHandler;

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
		ComponentReceiveXML eventMessage = myazureWeixinAPI.getEventMessage(request, response, ComponentReceiveXML.class);
		if (eventMessage == null) {
			outputStreamWrite(response.getOutputStream(), "false");
			return;
		}
		outputStreamWrite(response.getOutputStream(), "success");
		LOG.debug(MyazureConstants.LOG_SPLIT_LINE);
		LOG.debug(eventMessage.getInfoType());
		switch (eventMessage.getInfoType()) {
		case "component_verify_ticket":
			myazureWeixinAPI.refreshVerifyTicket(eventMessage);
			break;
		case "unauthorized":
			authorizeHandler.unauthorized(eventMessage);
			break;
		case "updateauthorized":
			authorizeHandler.updateauthorized(eventMessage);
			break;
		case "authorized":
			authorizeHandler.authorized(eventMessage);
			break;
		default:
			authorizeHandler.unknowEvent(eventMessage);
			break;
		}
		return;
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
		MaUser user = userService.getMaUserById(userId);
		ApiQueryAuthResult authInfoRes = myazureWeixinAPI.getAuthInfo(authCode);
		Authorization_info authInfo = authInfoRes.getAuthorization_info();
		ApiGetAuthorizerInfoResult userInfo = myazureWeixinAPI.getAuthUserInfo(authInfo.getAuthorizer_appid());
		Authorizer_info oaAccount = userInfo.getAuthorizer_info();
		MaOfficialAccount oaUpdate = maOfficialAccountService.findByAppId(authInfo.getAuthorizer_appid());
		if (oaUpdate == null) {
			oaUpdate = new MaOfficialAccount();
			oaUpdate.setAppId(authInfoRes.getAuthorization_info().getAuthorizer_appid());
		}
		oaUpdate.setAlias(userInfo.getAuthorizer_info().getAlias());
		oaUpdate.setQrcodeUrl(userInfo.getAuthorizer_info().getQrcode_url());
		oaUpdate.setAuthorized(authInfoRes.isSuccess());
		oaUpdate.setHeadImgUrl(oaAccount.getHead_img());
		oaUpdate.setNickName(oaAccount.getNick_name());
		oaUpdate.setRefreshToken(authInfo.getAuthorizer_refresh_token());
		oaUpdate.setUser(user);
		oaUpdate.setUserName(oaAccount.getUser_name());
		maOfficialAccountService.updateAdOfficialAccount(oaUpdate);
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
		EventMessage eventMessage = myazureWeixinAPI.getEventMessage(request, response, EventMessage.class);
		outputStreamWrite(response.getOutputStream(), "success");
		response.getOutputStream().flush();
		if (eventMessage == null) {
			LOG.error("[MyazureWeixin]: NOT FOUNDED Event Message:" + eventMessage);
//			TextMessage errMessage = new TextMessage(MyazureConstants.MYAZURE_WEIXIN_ADMIN_OPENID, "[MyazureWeixin]: NOT FOUNDED Event Message:" + eventMessage);
//			sendAdminMsg(errMessage);
			return;
		}
		LOG.debug("[MyazureWeixin]: eventMessage:" + eventMessage.toString());
		MaOfficialAccount oaAccount = maOfficialAccountService.findByAppId(appId);
		if (oaAccount == null) {
			LOG.debug("[MyazureWeixin]:oaAccount Not Founded,msg:{}", JSON.toJSONString(eventMessage));
//			TextMessage errMessage = new TextMessage(MyazureConstants.MYAZURE_WEIXIN_ADMIN_OPENID, "OaAccountNotFounded");
//			sendAdminMsg(errMessage);
			return;
		}
		switch (eventMessage.getMsgType()) {
		case "event":
			String eventString = eventMessage.getEvent();
			switch (eventString) {
			case "subscribe":
				LOG.debug("[MyazureWeixin]: VIEW event {}", eventMessage.getEvent());
				LOG.debug("[MyazureWeixin]: VIEW eventKey {}", eventMessage.getEventKey());
				LOG.debug("[MyazureWeixin]: VIEW eventMessage {}", JSON.toJSONString(eventMessage));
				String eventKey = eventMessage.getEventKey();
				if (eventKey.startsWith("qrscene_")) {
					// TODO
					// subscribe event
					// scan event
					
					
					
				} else {
					// TODO
					// Normal Subscribe
				}
				return;
			case "SCAN":
				//TODO 
				//scan things
				//
				return;
			case "unsubscribe":
				//TODO

				return;
			case "location":
				//TODO
				//save Location
				return;
			case "VIEW":
				LOG.debug("[MyazureWeixin]: VIEW event {}", eventMessage.getEvent());
				LOG.debug("[MyazureWeixin]: VIEW eventKey {}", eventMessage.getEventKey());
				LOG.debug("[MyazureWeixin]: VIEW eventMessage {}", JSON.toJSONString(eventMessage));
				return;
			case "CLICK":
				LOG.debug("[MyazureWeixin]: CLICK event {}", eventMessage.getEvent());
				LOG.debug("[MyazureWeixin]: CLICK eventKey {}", eventMessage.getEventKey());
				LOG.debug("[MyazureWeixin]: CLICK eventMessage {}", JSON.toJSONString(eventMessage));
				return;
			default:
				LOG.debug("[MyazureWeixin]: Default event {}", eventMessage.getEvent());
				LOG.debug("[MyazureWeixin]: Default eventKey {}", eventMessage.getEventKey());
				LOG.debug("[MyazureWeixin]: default eventMessage {}", JSON.toJSONString(eventMessage));
				break;
			}
			break;
		case "text":
			LOG.info("[MyazureWeixin]:  text MsgId is {}", eventMessage.getMsgId());
			LOG.info("[MyazureWeixin]:  text event is {}", eventMessage.getEvent());
			LOG.debug("[MyazureWeixin]:  text Content is {}", eventMessage.getContent());
			LOG.debug("[MyazureWeixin]: image eventMessage {}", JSON.toJSONString(eventMessage));
			break;
		case "image":
			LOG.info("[MyazureWeixin]: image MsgId is {}", eventMessage.getMsgId());
			LOG.debug("[MyazureWeixin]: image PicUrl is {}", eventMessage.getPicUrl());
			LOG.debug("[MyazureWeixin]: image MediaId is {}", eventMessage.getMediaId());
			LOG.debug("[MyazureWeixin]: image eventMessage {}", JSON.toJSONString(eventMessage));
			break;
		case "voice":
			LOG.info("[MyazureWeixin]: voice MsgId is {}", eventMessage.getMsgId());
			LOG.debug("[MyazureWeixin]: voice MediaId is {}", eventMessage.getMediaId());
			LOG.debug("[MyazureWeixin]: voice Format is {}", eventMessage.getFormat());
			LOG.debug("[MyazureWeixin]: voice eventMessage {}", JSON.toJSONString(eventMessage));
			break;
		case "video":
			LOG.info("[MyazureWeixin]: video MsgId is {}", eventMessage.getMsgId());
			LOG.debug("[MyazureWeixin]: video MediaId is {}", eventMessage.getMediaId());
			LOG.debug("[MyazureWeixin]: video ThumbMedia is {}", eventMessage.getThumbMediaId());
			LOG.debug("[MyazureWeixin]: video eventMessage {}", JSON.toJSONString(eventMessage));
			break;
		case "shortvideo":
			LOG.info("[MyazureWeixin]: Dispatch event, event is {}", eventMessage.getMsgId());
			LOG.debug("[MyazureWeixin]: Default event, msgType is {}", eventMessage.getMsgType());
			LOG.debug("[MyazureWeixin]: Default eventKey {}", eventMessage.getEventKey());
			LOG.debug("[MyazureWeixin]: Default eventMessage {}", JSON.toJSONString(eventMessage));
			break;
		case "location":
			LOG.info("[MyazureWeixin]: Dispatch event, event is {}", eventMessage.getMsgId());
			LOG.debug("[MyazureWeixin]: Default event, msgType is {}", eventMessage.getMsgType());
			LOG.debug("[MyazureWeixin]: Default eventKey {}", eventMessage.getEventKey());
			LOG.debug("[MyazureWeixin]: Default eventMessage {}", JSON.toJSONString(eventMessage));
			break;
		case "link":
			LOG.info("[MyazureWeixin]: Dispatch event, event is {}", eventMessage.getMsgId());
			LOG.debug("[MyazureWeixin]: Default event, msgType is {}", eventMessage.getMsgType());
			LOG.debug("[MyazureWeixin]: Default eventKey {}", eventMessage.getEventKey());
			LOG.debug("[MyazureWeixin]: Default eventMessage {}", JSON.toJSONString(eventMessage));
			break;
		default:
			LOG.info("[MyazureWeixin]: Dispatch event, event is {}", eventMessage.getMsgId());
			LOG.debug("[MyazureWeixin]: Default event, msgType is {}", eventMessage.getMsgType());
			LOG.info("[MyazureWeixin]: Default event, event is {}", eventMessage.getEvent());
			LOG.debug("[MyazureWeixin]: Default eventKey {}", eventMessage.getEventKey());
			LOG.debug("[MyazureWeixin]: Default eventMessage {}", JSON.toJSONString(eventMessage));
			break;
		}
//		TextMessage errMessage = new TextMessage(MyazureConstants.MYAZURE_WEIXIN_ADMIN_OPENID, "[MyazureWeixin]: NOT FOUNDED Event Message:" + eventMessage);
//		sendAdminMsg(errMessage);
		return;
	}

	private void sendAdminMsg(TextMessage msg2Send) {
		MessagePostman errMsg = new MessagePostman(msg2Send, TokenAPI.token(MyazureConstants.MYAZURE_WEIXIN_ADMIN_APPID,
				MyazureConstants.MYAZURE_WEIXIN_ADMIN_APPSECRET).getAccess_token());
		Thread toSendMsg = new Thread(errMsg);
		toSendMsg.start();
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