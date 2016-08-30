package org.myazure.weixin.constant;

import weixin.popular.support.ExpireKey;
import weixin.popular.support.expirekey.DefaultExpireKey;

public class WeixinConstans {
	public static ExpireKey expireKey = new DefaultExpireKey();
	public static final String BASE_WEIXIN_API_URI = "https://api.weixin.qq.com";
	public static final String PARAM_ACCESS_TOKEN = "access_token";
	public static final String TOKEN_STRING = "token";
	public static final String COMPONENT_ACCESS_TOKEN_KEY = "component.access.token";
	public static final String PRE_AUTH_CODE_KEY = "pre.auth.code";
	public static final String AUTHORIZER_ACCESS_TOKEN_KEY = "authorizer.access.token";
	public static final String AUTHORIZER_REFRESH_TOKEN_KEY = "authorizer.refresh.token";
	// WECHAT MSG INFO
	public static final int FUNCTION_MSG_MANAGEMENT = 1;// 消息管理权限
	public static final int FUNCTION_USER_MANAGEMENT = 2;
	public static final int FUNCTION_ACCOUNT_SERVICE_MANAGEMENT = 3;
	public static final int FUNCTION_WEB_SERVICE_MANAGEMENT = 4;
	public static final int FUNCTION_WEIXIN_STORE_MANAGEMENT = 5;
	public static final int FUNCTION_VICTOR_CUSTORMER_SERVICE_MANAGEMENT = 6;
	public static final int FUNCTION_MASS_NOTIFICATION_MANAGEMENT = 7;
	public static final int FUNCTION_WEIXIN_CARD_MANAGEMENT = 8;
	public static final int FUNCTION_SWEEP_MANAGEMENT = 9;
	public static final int FUNCTION_EVEN_FIWI_MANAGEMENT = 10;
	public static final int FUNCTION_MATERIAL_MANAGEMENT = 11;
	public static final int FUNCTION_WECHAT_SHAKE_PERIPHERAL_MANAGEMENT = 12;
	public static final int FUNCTION_WECHAT_STORE_MANAGEMENT = 13;
	public static final int FUNCTION_PAYMENT_MANAGEMENT = 14;
	public static final int FUNCTION_CUSTOM_MENU_MANAGEMENT = 15;
}
