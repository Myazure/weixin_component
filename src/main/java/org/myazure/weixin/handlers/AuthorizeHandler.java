package org.myazure.weixin.handlers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;

import org.myazure.weixin.MyazureWeixinAPI;
import org.myazure.weixin.constant.MyazureConstants;
import org.myazure.weixin.constant.WeixinConstans;
import org.myazure.weixin.domain.MaOfficialAccount;
import org.myazure.weixin.service.MaOfficialAccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;

import com.alibaba.fastjson.JSON;

import weixin.popular.api.ComponentAPI;
import weixin.popular.bean.component.ApiGetAuthorizerInfoResult;
import weixin.popular.bean.component.ApiQueryAuthResult;
import weixin.popular.bean.component.AuthorizerAccessToken;
import weixin.popular.bean.component.ComponentAccessToken;
import weixin.popular.bean.component.ComponentReceiveXML;
import weixin.popular.bean.component.PreAuthCode;
import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;
@Controller
public class AuthorizeHandler {

	private static final Logger LOG = LoggerFactory.getLogger(AuthorizeHandler.class);
	@Autowired
	private     MaOfficialAccountService officialAccountService;
	@Autowired
	private     StringRedisTemplate redisTemplate;
	@Autowired
	private MyazureWeixinAPI myazureWeixinAPI;
	/**
	 * 获取公众号第三方平台access_token<br />
	 * YSW_APP_ID 公众号第三方平台appid<br />
	 * YSW_APP_SECRET 公众号第三方平台appsecret<br />
	 * YSW_Component_Verify_Ticket 微信后台推送的ticket，此ticket会定时推送，具体请见推送说明<br />
	 * 
	 * @return 公众号第三方平台access_token
	 */
	public   String getComponentAccessTokenStr() {
		LOG.debug("[Myazure Weixin]: Get >>>Component Access Token<<< from redis.");
		String accessToken = redisTemplate.opsForValue().get(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY);
		if (null == accessToken || accessToken.trim().length() == 0) {
			LOG.info("[Myazure Weixin]: Get >>>Component Access Token<<< from WX.");
			ComponentAccessToken res = ComponentAPI.api_component_token(MyazureConstants.MYAZURE_APP_ID, MyazureConstants.MYAZURE_APP_SECRET,
					MyazureConstants.MYAZURE_COMPONENT_VERIFY_TICKET);
			accessToken = res.getComponent_access_token();
			if (null != accessToken) {
				redisTemplate.opsForValue().set(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY, accessToken, res.getExpires_in() - 60,
						TimeUnit.SECONDS);
			}
		}
		return accessToken;
	}

	/**
	 * 获取公众号第三方平台access_token<br />
	 * YSW_APP_ID 公众号第三方平台appid<br />
	 * YSW_PRE_AUTH_CODE 公众号第三方平台YSW_PRE_AUTH_CODE<br />
	 * 
	 * @param calbackurl
	 *            回掉地址<br />
	 * @return 公众号第三方平台access_token
	 */
	public String componentLoginPage(String calbackurl) {
		try {
			StringBuilder sb = new StringBuilder();
			// TODO
			// Remove this at version of weixin-popular over 2.8
			sb.append("https://mp.weixin.qq.com" + "/cgi-bin/componentloginpage?").append("component_appid=").append(MyazureConstants.MYAZURE_APP_ID)
					.append("&pre_auth_code=").append(this.getPreAuthCodeStr()).append("&redirect_uri=").append(URLEncoder.encode(calbackurl, "utf-8"));
			return sb.toString();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public String getPreAuthCodeStr() {
		// Fetch from redis first
		LOG.info("[Myazure Weixin]: Get >>>Pre Auth Code<<< from redis.");
		String preAuthCode = redisTemplate.opsForValue().get(WeixinConstans.PRE_AUTH_CODE_KEY);
		if (null == preAuthCode || preAuthCode.trim().length() == 0) {
			LOG.info("[Myazure Weixin]: Get >>>Pre Auth Code<<< from WX.");
			PreAuthCode res = ComponentAPI.api_create_preauthcode(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN, MyazureConstants.MYAZURE_APP_ID);
			preAuthCode = res.getPre_auth_code();
		}
		PreAuthCode res2 = ComponentAPI.api_create_preauthcode(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN, MyazureConstants.MYAZURE_APP_ID);
		if (null != res2.getPre_auth_code()) {
			redisTemplate.opsForValue().set(WeixinConstans.PRE_AUTH_CODE_KEY, res2.getPre_auth_code(), res2.getExpires_in() - 60,
					TimeUnit.SECONDS);
		}
		return preAuthCode;
	}

	public String getAuthorizerRefreshTokenStr(String authorizer_appid) {
		String key = genAuthorizerRefreshTokenKey(authorizer_appid);
		LOG.info("[Myazure Weixin]: Get >>>Authorizer Refresh Token<<< from redis.");
		String authorizerRefreshToken = redisTemplate.opsForValue().get(key);
		if (null == authorizerRefreshToken || authorizerRefreshToken.trim().length() == 0) {
			LOG.info("[Myazure Weixin]: Get >>>Authorizer Refresh Token<<< from DB.   ID" + authorizer_appid);
			MaOfficialAccount oa = officialAccountService.findByAppId(authorizer_appid);
			authorizerRefreshToken = oa.getRefreshToken();
			if (null != authorizerRefreshToken) {
				redisTemplate.opsForValue().set(key, authorizerRefreshToken);
			}
		}
		return authorizerRefreshToken;
	}

	/**
	 * 使用授权码换取公众号的授权信息<br />
	 * component_access_token component_access_token <br />
	 * YSW_APP_ID 公众号第三方平台appid<br />
	 * 
	 * @param authorization_code
	 *            授权code,会在授权成功时返回给第三方平台，详见第三方平台授权流程说明
	 * @return 公众号的授权信息
	 */
	public ApiQueryAuthResult getAuthInfo(String authorizationCode) {
		ApiQueryAuthResult auth = ComponentAPI.api_query_auth(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN, MyazureConstants.MYAZURE_APP_ID,
				authorizationCode);
		if (null != auth) {
			Authorization_info authInfo = auth.getAuthorization_info();
			if (null != authInfo) {
				LOG.info("[Myazure Weixin]: Store >>>Authorizer Access Token<<< to redis.");
				String key = genAuthorizerAccessTokenKey(authInfo.getAuthorizer_appid());
				redisTemplate.opsForValue().set(key, authInfo.getAuthorizer_access_token(), authInfo.getExpires_in() - 60, TimeUnit.SECONDS);
				LOG.info("[Myazure Weixin]: Store >>>Authorizer Refresh Token<<< to redis.");
				key = genAuthorizerRefreshTokenKey(authInfo.getAuthorizer_appid());
				redisTemplate.opsForValue().set(key, authInfo.getAuthorizer_refresh_token());
			}
		}
		return auth;
	}

	/**
	 * 获取授权方的账户信息<br />
	 * component_access_token<br />
	 * YSW_APP_ID 服务appid<br />
	 * 
	 * @param authorizer_appid
	 *            授权方appid<br />
	 * @return 授权方的账户信息
	 */
	public ApiGetAuthorizerInfoResult getAuthUserInfo(String authorizer_appid) {
		return apiGetAuthorizerInfo(authorizer_appid);
	}

	/**
	 * 获取授权方的账户信息<br />
	 * component_access_token component_access_token<br />
	 * YSW_APP_ID 服务appid<br />
	 * 
	 * @param authorizer_appid
	 *            授权方appid<br />
	 * @return 授权方的账户信息
	 */
	public ApiGetAuthorizerInfoResult apiGetAuthorizerInfo(String authorizer_appid) {
		return ComponentAPI.api_get_authorizer_info(getComponentAccessTokenStr(), MyazureConstants.MYAZURE_APP_ID, authorizer_appid);
	}

	/**
	 * 获取（刷新）授权公众号的令牌<br />
	 * authorizer_refresh_token 刷新token<br />
	 * authorizer_appid 公众号被授权appid<br />
	 * 
	 * @param authorizer_appid
	 *            授权方appid<br />
	 * @param authorizer_refresh_token
	 *            授权方的刷新令牌，刷新令牌主要用于公众号第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，
	 *            请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌<br />
	 * @return AuthorizerAccessToken 授权公众号的令牌
	 */
	public AuthorizerAccessToken getAuthToken(String authorizer_appid, String authorizer_refresh_token) {
		return ComponentAPI.api_authorizer_token(getComponentAccessTokenStr(), MyazureConstants.MYAZURE_APP_ID, authorizer_appid, authorizer_refresh_token);
	}

	/**
	 * 授权者的刷新码
	 * 
	 * @param authorizerAppId
	 * @return
	 */
	private static String genAuthorizerRefreshTokenKey(String authorizerAppId) {
		return WeixinConstans.AUTHORIZER_REFRESH_TOKEN_KEY + "[" + authorizerAppId + "]:";
	}

	/**
	 * 授权者的授权码
	 * 
	 * @param authorizerAppId
	 * @return
	 */
	private static String genAuthorizerAccessTokenKey(String authorizerAppId) {
		return WeixinConstans.AUTHORIZER_ACCESS_TOKEN_KEY + "[" + authorizerAppId + "]:";
	}

	public   void unauthorized(ComponentReceiveXML eventMessage) {
		LOG.debug(MyazureConstants.LOG_SPLIT_LINE);
		LOG.debug("[Myazure Weixin]: unauthorized:{} , Authorizer:{}",  eventMessage.getAppId(),eventMessage.getAuthorizerAppid());
		MaOfficialAccount oaUpdate = null;
		if (null==officialAccountService.findByAppId(eventMessage.getAuthorizerAppid())) {
			oaUpdate = new MaOfficialAccount();
			oaUpdate.setAppId(eventMessage.getAuthorizerAppid());
		}else {
			oaUpdate=officialAccountService.findByAppId(eventMessage.getAuthorizerAppid());
		}
		ApiGetAuthorizerInfoResult info=ComponentAPI.api_get_authorizer_info(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN, MyazureConstants.MYAZURE_APP_ID, eventMessage.getAuthorizerAppid());
		oaUpdate.setAlias(info.getAuthorizer_info().getAlias());
		oaUpdate.setQrcodeUrl(info.getAuthorizer_info().getQrcode_url());
		oaUpdate.setAuthorized(eventMessage.getAuthorizationCode() != null ? true : false);
		oaUpdate.setRefreshToken(myazureWeixinAPI.getAuthInfo(eventMessage.getAuthorizationCode()).getAuthorization_info().getAuthorizer_refresh_token());
		officialAccountService.updateAdOfficialAccount(oaUpdate);
		return;
	}

	public   void updateauthorized(ComponentReceiveXML eventMessage) {
		LOG.debug(MyazureConstants.LOG_SPLIT_LINE);
		LOG.debug("[YSW Adsense]: updateauthorized:" + eventMessage.getAppId());
		MaOfficialAccount oaUpdate = officialAccountService.findByAppId(eventMessage.getAuthorizerAppid());
		if (oaUpdate == null) {
			oaUpdate = new MaOfficialAccount();
			oaUpdate.setAppId(eventMessage.getAuthorizerAppid());
		}
		ApiGetAuthorizerInfoResult info=ComponentAPI.api_get_authorizer_info(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN, MyazureConstants.MYAZURE_APP_ID, eventMessage.getAuthorizerAppid());
		oaUpdate.setAlias(info.getAuthorizer_info().getAlias());
		oaUpdate.setQrcodeUrl(info.getAuthorizer_info().getQrcode_url());
		oaUpdate.setAuthorized(eventMessage.getAuthorizationCode() != null ? true : false);
		oaUpdate.setRefreshToken(myazureWeixinAPI.getAuthInfo(eventMessage.getAuthorizationCode()).getAuthorization_info().getAuthorizer_refresh_token());
		// checkAuthorize(MyazureWeixinAPI.getAuthInfo(eventMessage.getAuthorizationCode()).getAuthorization_info().getFunc_info());
		officialAccountService.updateAdOfficialAccount(oaUpdate);
		return;
	}

	public   void authorized(ComponentReceiveXML eventMessage) {
		LOG.debug(MyazureConstants.LOG_SPLIT_LINE);
		LOG.error("[Myazure Weixin]:   authorized:{} ", eventMessage.getAuthorizerAppid());
		LOG.debug("[Myazure Weixin]:  event message:{}", JSON.toJSONString(eventMessage));
		MaOfficialAccount oaAuthorized = officialAccountService.findByAppId(eventMessage.getAuthorizerAppid());
		if (oaAuthorized == null) {
			oaAuthorized = new MaOfficialAccount();
			oaAuthorized.setAppId(eventMessage.getAuthorizerAppid());
		}
		ApiGetAuthorizerInfoResult info=ComponentAPI.api_get_authorizer_info(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN, MyazureConstants.MYAZURE_APP_ID, eventMessage.getAuthorizerAppid());
		oaAuthorized.setAlias(info.getAuthorizer_info().getAlias());
		oaAuthorized.setQrcodeUrl(info.getAuthorizer_info().getQrcode_url());
		info.getAuthorization_info().getFunc_info();
		info.getAuthorization_info().getAppid();
		info.getAuthorizer_info().getAlias();
		info.getAuthorizer_info().getBusiness_info();
		info.getAuthorizer_info().getHead_img();
		info.getAuthorizer_info().getNick_name();
		info.getAuthorizer_info().getQrcode_url();
		info.getAuthorizer_info().getService_type_info();
		info.getAuthorizer_info().getUser_name();
		info.getAuthorizer_info().getVerify_type_info();
		
		
		
		
		
		oaAuthorized.setAuthorized(eventMessage.getAuthorizationCode() != null ? true : false);
		oaAuthorized.setRefreshToken(myazureWeixinAPI.getAuthInfo(eventMessage.getAuthorizationCode()).getAuthorization_info().getAuthorizer_refresh_token());
		officialAccountService.updateAdOfficialAccount(oaAuthorized);
		return;
	}

	public   void unknowEvent(ComponentReceiveXML eventMessage) {
		LOG.debug(MyazureConstants.LOG_SPLIT_LINE);
		LOG.error("[Myazure Weixin]: NOT FOUNDED ! Event Type: ", eventMessage.getInfoType());
	}

}
