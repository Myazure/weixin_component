package com.ysw.adsense.service;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weixin.popular.support.ExpireKey;
import weixin.popular.support.expirekey.DefaultExpireKey;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

public final class BASE {
	private static final Logger LOG = LoggerFactory.getLogger(BASE.class);
	public static final String LOG_SPLIT_LINE = "================================================================================";
	public static final String TOKEN_STRING = "token";
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
	public static String SERVER_ID;
	public static String YSW_APP_ID;
	public static String YSW_APP_SECRET;
	public static String YSW_ENCODE_TOKEN;
	public static String YSW_ENCODE_KEY;
	public static String YSW_APP_URL;
	public static String YSW_IP_URL;
	public static String YSW_COMPONENT_VERIFY_TICKET;
	public static String YSW_COMPONENT_ACCESS_TOKEN;
	public static String YSW_PRE_AUTH_CODE;
	public static WXBizMsgCrypt WXBIZMSGCRYPT;
	public static ExpireKey expireKey = new DefaultExpireKey();
	public static void initWXBizMsgCrypt() {
		try {
			WXBIZMSGCRYPT = new WXBizMsgCrypt(BASE.YSW_ENCODE_TOKEN,
					BASE.YSW_ENCODE_KEY, BASE.YSW_APP_ID);
		} catch (AesException e) {
			LOG.error(e.getMessage() + "00000000000000WXCRASHHHHHHHHHHHHHHHHHHHHH000000000000000000");
		}
	}

}
