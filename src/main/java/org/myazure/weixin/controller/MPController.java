package org.myazure.weixin.controller;

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
import org.myazure.weixin.configuration.AppUrlService;
import org.myazure.weixin.constant.MyazureConstants;
import org.myazure.weixin.domain.MaOfficialAccount;
import org.myazure.weixin.domain.MaOfficialAccountWxUser;
import org.myazure.weixin.domain.MaUser;
import org.myazure.weixin.domain.MaWxUser;
import org.myazure.weixin.domain.CurrentUser;
import org.myazure.weixin.service.AdUserService;
import org.myazure.weixin.service.AdsenseAPI;
import org.myazure.weixin.service.BASE;
import org.myazure.weixin.utils.LocationUtils;
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
	private AdUserService userService;


	@Autowired
	private AdsenseAPI adsenseAPI;

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
		
		LOG.debug(MyazureConstants.LOG_SPLIT_LINE);
		LOG.debug(eventMessage.getInfoType());
		switch (eventMessage.getInfoType()) {
		case "component_verify_ticket":
			adsenseAPI.refresh(eventMessage.getComponentVerifyTicket());
			break;
		case "unauthorized":
			LOG.debug(MyazureConstants.LOG_SPLIT_LINE);
			LOG.debug("[Myazure Weixin]: unauthorized:" + eventMessage.getAppId());
			break;
		case "updateauthorized":
			LOG.debug(MyazureConstants.LOG_SPLIT_LINE);
			LOG.debug("[Myazure Weixin]: updateauthorized:" + eventMessage.getAppId());
			break;
		case "authorized":
			LOG.debug(MyazureConstants.LOG_SPLIT_LINE);
			LOG.debug("[Myazure Weixin]: authorized:" + eventMessage.getAppId());
			break;
		default:
			LOG.debug(MyazureConstants.LOG_SPLIT_LINE);
			LOG.error("[Myazure Weixin]: NOT FOUNDED ! Event Type: ", eventMessage.getInfoType());
			break;
		}
		outputStreamWrite(response.getOutputStream(), "success");
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
		MaUser user = userService.getAdUserById(userId);
		ApiQueryAuthResult authInfoRes = adsenseAPI.getAuthInfo(authCode);
		Authorization_info authInfo = authInfoRes.getAuthorization_info();
		ApiGetAuthorizerInfoResult userInfo = adsenseAPI.getAuthUserInfo(authInfo.getAuthorizer_appid());
		Authorizer_info oaAccount = userInfo.getAuthorizer_info();
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
		return;
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