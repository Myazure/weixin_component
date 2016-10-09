package org.myazure.weixin;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.myazure.weixin.constant.MyazureConstants;
import org.myazure.weixin.constant.WeixinConstans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import weixin.popular.api.ComponentAPI;
import weixin.popular.api.QrcodeAPI;
import weixin.popular.api.TicketAPI;
import weixin.popular.api.UserAPI;
import weixin.popular.bean.component.ApiGetAuthorizerInfoResult;
import weixin.popular.bean.component.ApiQueryAuthResult;
import weixin.popular.bean.component.ApiQueryAuthResult.Authorization_info;
import weixin.popular.bean.component.AuthorizerAccessToken;
import weixin.popular.bean.component.ComponentAccessToken;
import weixin.popular.bean.component.ComponentReceiveXML;
import weixin.popular.bean.component.FuncInfo;
import weixin.popular.bean.component.PreAuthCode;
import weixin.popular.bean.message.EventMessage;
import weixin.popular.bean.qrcode.QrcodeTicket;
import weixin.popular.bean.ticket.Ticket;
import weixin.popular.bean.user.User;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.util.EmojiUtil;
import weixin.popular.util.SignatureUtil;
import weixin.popular.util.StreamUtils;
import weixin.popular.util.XMLConverUtil;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;
/**
 * 
 * @author WangZhen
 *
 */
@Service
public class MyazureWeixinAPI {

	private static final Logger LOG = LoggerFactory.getLogger(MyazureWeixinAPI.class);


	@Autowired
	private   StringRedisTemplate redisTemplate;

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

	/**
	 * 获取公众号第三方平台access_token<br />
	 * YSW_APP_ID 公众号第三方平台appid<br />
	 * YSW_APP_SECRET 公众号第三方平台appsecret<br />
	 * YSW_Component_Verify_Ticket 微信后台推送的ticket，此ticket会定时推送，具体请见推送说明<br />
	 * 
	 * @return 公众号第三方平台access_token
	 */
	public   String getComponentAccessTokenStr() {
		// Fetch from redis first
		LOG.info("[Myazure Weixin]: Get >>>Component Access Token<<< from redis.");
		String accessToken = redisTemplate.opsForValue().get(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY);
		if (null == accessToken || accessToken.trim().length() == 0) {
			LOG.info("[Myazure Weixin]: Get >>>Component Access Token<<< from WX.");
			ComponentAccessToken res = ComponentAPI.api_component_token(MyazureConstants.MYAZURE_APP_ID, MyazureConstants.MYAZURE_APP_SECRET,
					MyazureConstants.MYAZURE_COMPONENT_VERIFY_TICKET);
			accessToken = res.getComponent_access_token();
			if (null != accessToken) {
				redisTemplate.opsForValue().set(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY, accessToken, res.getExpires_in() - 60, TimeUnit.SECONDS);
			}
		}
		return accessToken;
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
			// Save to redis
			redisTemplate.opsForValue().set(WeixinConstans.PRE_AUTH_CODE_KEY, res2.getPre_auth_code(), res2.getExpires_in() - 60, TimeUnit.SECONDS);
		}
		return preAuthCode;
	}

	public String getAuthorizerRefreshTokenStr(String authorizer_appid) {
		String key = MyazureWeixinAPI.genAuthorizerRefreshTokenKey(authorizer_appid);
		// Fetch from redis first
		LOG.info("[Myazure Weixin]: Get >>>Authorizer Refresh Token<<< from redis.");
		String authorizerRefreshToken = redisTemplate.opsForValue().get(key);
		if (null == authorizerRefreshToken || authorizerRefreshToken.trim().length() == 0) {
			// Get authorizer refresh token
			LOG.info("[Myazure Weixin]: Get >>>Authorizer Refresh Token<<< from DB.");
			if (null != authorizerRefreshToken) {
				// Save to redis
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
	public   ApiQueryAuthResult getAuthInfo(String authorizationCode) {
		ApiQueryAuthResult auth = ComponentAPI.api_query_auth(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN, MyazureConstants.MYAZURE_APP_ID, authorizationCode);
		if (null != auth) {
			Authorization_info authInfo = auth.getAuthorization_info();
			if (null != authInfo) {
				LOG.info("[Myazure Weixin]: Store >>>Authorizer Access Token<<< to redis.");
				String key = MyazureWeixinAPI.genAuthorizerAccessTokenKey(authInfo.getAuthorizer_appid());
				redisTemplate.opsForValue().set(key, authInfo.getAuthorizer_access_token(), authInfo.getExpires_in() - 60, TimeUnit.SECONDS);
				LOG.info("[Myazure Weixin]: Store >>>Authorizer Refresh Token<<< to redis.");
				key = MyazureWeixinAPI.genAuthorizerRefreshTokenKey(authInfo.getAuthorizer_appid());
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
		return apiAuthorizerToken(authorizer_appid, authorizer_refresh_token);
	}

	/**
	 * 获取（刷新）授权公众号的令牌<br />
	 * authorizer_refresh_token authorizer_refresh_token<br />
	 * authorizer_appid 公众号授权的 appid<br />
	 * 
	 * @param authorizer_appid
	 *            授权方appid
	 * @param authorizer_refresh_token
	 *            授权方的刷新令牌，刷新令牌主要用于公众号第三方平台获取和刷新已授权用户的access_token，只会在授权时刻提供，
	 *            请妥善保存。 一旦丢失，只能让用户重新授权，才能再次拿到新的刷新令牌
	 * @return AuthorizerAccessToken 授权公众号的令牌
	 */
	public AuthorizerAccessToken apiAuthorizerToken(String authorizer_appid, String authorizer_refresh_token) {
		return ComponentAPI.api_authorizer_token(getComponentAccessTokenStr(), MyazureConstants.MYAZURE_APP_ID, authorizer_appid, authorizer_refresh_token);
	}

	
	/**
	 * 获取 jsapi_ticket
	 * 
	 * @param access_token
	 *            access_token
	 * @return Ticket
	 */
	public Ticket getJSApiTicket() {
		return TicketAPI.ticketGetticket(getMyazureAccessToken());
	}

	/**
	 * 获取 access_token
	 * 
	 * @param MYAZURE_APP_ID
	 *            YSW_APP_ID
	 * @return access_token
	 */
	public String getMyazureAccessToken() {
		return MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN;
	}

	/**
	 * 解密数据包，返回传参类的对象
	 * 
	 * @author WangZhen
	 * 
	 * @param request
	 *            传入server接到的请求request
	 * @param response
	 *            服务器response
	 * @param clazz
	 *            解析数据格式的类
	 * @return T 根据传入解析数据格式的类的数据实例
	 */
	public <T> T getEventMessage(HttpServletRequest request, HttpServletResponse response, Class<T> clazz) throws IOException {
		return getEventMessage(request, response, clazz, MyazureConstants.MYAZUZRE_WXBIZMSGCRYPT, MyazureConstants.MYAZURE_ENCODE_TOKEN);
	}

	/**
	 * 获取用户基本信息
	 * 
	 * @param access_token
	 *            access_token
	 * @param openid
	 *            openid
	 * @return User
	 */
	public User getUserInfo(String access_token, String openid) {
		return this.userInfo(access_token, openid);
	}

	/**
	 * 获取用户基本信息
	 * 
	 * @param access_token
	 *            access_token
	 * @param openid
	 *            openid
	 * @param emoji
	 *            表情解析方式<br>
	 *            0 不设置 <br>
	 *            1 HtmlHex 格式<br>
	 *            2 HtmlTag 格式<br>
	 *            3 Alias 格式<br>
	 *            4 HtmlDec 格式<br>
	 *            5 PureText 纯文本<br>
	 * @return User
	 */
	public User userInfo(String access_token, String openid, int emoji) {
		User user = UserAPI.userInfo(access_token, openid, emoji);
		if (emoji != 0 && user != null && user.getNickname() != null) {
			user.setNickname_emoji(EmojiUtil.parse(user.getNickname(), emoji));
		}
		return user;
	}

	/**
	 * 获取用户基本信息
	 * 
	 * @param access_token
	 *            access_token
	 * @param openid
	 *            openid
	 * @return User
	 */
	public User userInfo(String access_token, String openid) {
		return userInfo(access_token, openid, 0);
	}

	public QrcodeTicket getQRCodeTicketFinalStr(String access_token, String scene_id) {
		return this.qrcodeCreateFinal(access_token, scene_id);
	}

	public BufferedImage showQRTicket(String ticket) {
		return QrcodeAPI.showqrcode(ticket);
	}

	/**
	 * 创建二维码
	 * 
	 * @param access_token
	 *            access_token
	 * @param qrcodeJson
	 *            json 数据
	 * @return QrcodeTicket
	 */
	private QrcodeTicket qrcodeCreate(String access_token, int scene_id) {
		return QrcodeAPI.qrcodeCreateFinal(access_token, scene_id);
	}

	/**
	 * 创建临时二维码
	 * 
	 * @param access_token
	 *            access_token
	 * @param expire_seconds
	 *            最大不超过604800秒（即30天）
	 * @param scene_id
	 *            场景值ID，32位非0整型 最多10万个
	 * @return QrcodeTicket
	 */
	public QrcodeTicket qrcodeCreateTemp(String access_token, int expire_seconds, long scene_id) {
		return QrcodeAPI.qrcodeCreateTemp(access_token, expire_seconds, scene_id);
	}

	/**
	 * 创建持久二维码
	 * 
	 * @param access_token
	 *            access_token
	 * @param scene_id
	 *            场景值ID 1-100000
	 * @return QrcodeTicket
	 */
	public QrcodeTicket qrcodeCreateFinal(String access_token, int scene_id) {
		return QrcodeAPI.qrcodeCreateFinal(access_token, scene_id);
	}

	/**
	 * 创建持久二维码
	 * 
	 * @param access_token
	 *            access_token
	 * @param scene_str
	 *            场景值ID（字符串形式的ID），字符串类型，长度限制为1到64
	 * @return QrcodeTicket
	 */
	public QrcodeTicket qrcodeCreateFinal(String access_token, String scene_str) {
		return QrcodeAPI.qrcodeCreateFinal(access_token, scene_str);
	}

	/**
	 * 解密数据包，返回传参类的对象
	 * 
	 * @author WangZhen
	 * @param request
	 *            传入server接到的请求request
	 * @param response
	 *            服务器response
	 * @param clazz
	 *            解析数据格式的类
	 * @param wxBizMsgCrypt
	 *            微信的消息解密类
	 * @param encodeToken
	 *            微信后台获取的第三方的encodeToken
	 * @return T 根据传入解析数据格式的类的数据实例
	 */
	public <T> T getEventMessage(HttpServletRequest request, HttpServletResponse response, Class<T> clazz, WXBizMsgCrypt wxBizMsgCrypt, String encodeToken)
			throws IOException {
		ServletInputStream inputStream = request.getInputStream();
		ServletOutputStream outputStream = response.getOutputStream();
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		String encrypt_type = request.getParameter("encrypt_type");
		String msg_signature = request.getParameter("msg_signature");
		boolean isAes = "aes".equals(encrypt_type);
		T eventMessage = null;
		if (isAes && echostr != null) {
			try {
				echostr = URLDecoder.decode(echostr, "utf-8");
				String echostr_decrypt = wxBizMsgCrypt.verifyUrl(msg_signature, timestamp, nonce, echostr);
				outputStreamWrite(outputStream, echostr_decrypt);
				return null;
			} catch (AesException e) {
				e.printStackTrace();
				return null;
			}
		} else if (echostr != null) {
			outputStreamWrite(outputStream, echostr);
			return null;
		}
		if (isAes) {
			if (signature==null) {
				LOG.error("The request signature is invalid");
				return null;
			}
			if (!signature.equals(SignatureUtil.generateEventMessageSignature(encodeToken, timestamp, nonce))) {
				LOG.error("The request signature is invalid");
				return null;
			}
			try {
				String postData = StreamUtils.copyToString(inputStream, Charset.forName("utf-8"));
				String xmlData = wxBizMsgCrypt.decryptMsg(msg_signature, timestamp, nonce, postData);
				eventMessage = XMLConverUtil.convertToObject(clazz, xmlData);
			} catch (AesException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			if (!signature.equals(SignatureUtil.generateEventMessageSignature(encodeToken, timestamp, nonce))) {
				LOG.error("The request signature is invalid");
				return null;
			}
			if (inputStream != null) {
				eventMessage = XMLConverUtil.convertToObject(clazz, inputStream);
			}
		}

		if (clazz.getName().equals(EventMessage.class.getName())) {
			String key = ((EventMessage) eventMessage).getFromUserName() + "__" + ((EventMessage) eventMessage).getCreateTime();
			if (WeixinConstans.expireKey.exists(key)) {
				return null;
			} else {
				WeixinConstans.expireKey.add(key);
			}
		}
		return eventMessage;
	}

	/**
	 * 往输出流上输出数据
	 * 
	 * @param outputStream
	 *            输出流
	 * @param text
	 *            对输出流要输出的文本
	 * @return boolean 是否输出是成功
	 */

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

	/**
	 * 刷新凭证
	 * 
	 * @param componentVerifyTicket
	 */
	public void refreshVerifyTicket(ComponentReceiveXML eventMessage) {
		// Update component verify ticket in memory
		MyazureConstants.MYAZURE_COMPONENT_VERIFY_TICKET = eventMessage.getComponentVerifyTicket();
		// Refresh component access token
		LOG.info("[Myazure Weixin]: Refresh component access token from redis.");
		String accessToken = redisTemplate.opsForValue().get(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY);
		if (null == accessToken || accessToken.trim().length() == 0) {
			MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN = this.getComponentAccessTokenStr();
		} else if (redisTemplate.getExpire(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY) < 1000) {
			MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN = this.getComponentAccessTokenStr();
		}
		if(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN==null){
			MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN = redisTemplate.opsForValue().get(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY);
		}
		LOG.debug("[Myazure Weixin]: COMPONENT VIRFY TIKET NOW = {}", MyazureConstants.MYAZURE_COMPONENT_VERIFY_TICKET);
		LOG.debug("[Myazure Weixin]: Myazure COMPONENT ACCESS TOKEN NOW = {}", MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN);
		LOG.debug("[Myazure Weixin]: COMPONENT ACCESS TOKEN TIME= {}", redisTemplate.getExpire(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY));
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
	 * 获取授权者权限集是否满足
	 * 
	 * @param appidString
	 * @param func
	 * @return
	 */
	private boolean checkFuncInfo(String appidString, int func) {
		ApiGetAuthorizerInfoResult req = ComponentAPI.api_get_authorizer_info(MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN, MyazureConstants.MYAZURE_APP_ID,
				appidString);
		if (appidString == null | appidString == "") {
			LOG.debug("appidString == null||||||||| ");
			return false;
		}
		weixin.popular.bean.component.ApiGetAuthorizerInfoResult.Authorization_info authorization_info = req.getAuthorization_info();
		if (authorization_info == null) {
			LOG.debug("authorization_info == null||||||||| ");
			return false;
		}
		List<FuncInfo> appFuncInfo = authorization_info.getFunc_info();
		if (appFuncInfo == null) {
			LOG.debug("appFuncInfo == null||||||||| ");
			return false;
		}
		List<Integer> appFuncInfoIntegers = new ArrayList<Integer>();
		for (FuncInfo funcInfo : appFuncInfo) {
			appFuncInfoIntegers.add(funcInfo.getFuncscope_category().getId());
		}
		return appFuncInfoIntegers.contains(func);
	}

	/**
	 * 检查公众号是否授权用户管理权限
	 * 
	 * @param appidString
	 * @return
	 */
	private boolean checkUserManagementAccess(String appidString) {
		if (appidString == "" | appidString == null) {
			LOG.error("appidString is Null", appidString);
			return false;
		}
		if (checkFuncInfo(appidString, WeixinConstans.FUNCTION_USER_MANAGEMENT)) {
			LOG.info("Fuction Check Sucess!!@" + appidString);
			return true;
		} else {
			LOG.error("APP Permission denied.        NO FUNCTION_USER_MANAGEMENT");
		}
		return false;
	}

}
