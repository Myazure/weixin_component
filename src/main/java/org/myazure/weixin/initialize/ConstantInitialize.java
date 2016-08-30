package org.myazure.weixin.initialize;

import org.myazure.weixin.constant.MyazureConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import weixin.popular.api.API;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.support.TokenManager;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

public class ConstantInitialize {
	private static final Logger LOG = LoggerFactory.getLogger(ConstantInitialize.class);
	@Value("${weixin.compAppId}")
	protected String MYAZURE_APP_ID;
	@Value("${weixin.compAppSecret}")
	protected String MYAZURE_APP_SECRET;
	@Value("${weixin.encode.token}")
	protected String MYAZURE_ENCODE_TOKEN;
	@Value("${weixin.encode.key}")
	protected String MYAZURE_ENCODE_KEY;
	@Value("${myazure.appUrl}")
	protected String MYAZURE_APP_URL;
	@Value("${myazure.serverId}")
	protected String MYAZURE_SERVER_ID;

	public void init() {
		initMyazureWeixinConfig();
		checkInit();
		initWXBizMsgCrypt();
		initTokenManager();
	}

	public void initMyazureWeixinConfig() {
		MyazureConstants.MYAZURE_APP_ID = MYAZURE_APP_ID;
		MyazureConstants.MYAZURE_APP_SECRET = MYAZURE_APP_SECRET;
		MyazureConstants.MYAZURE_ENCODE_TOKEN = MYAZURE_ENCODE_TOKEN;
		MyazureConstants.MYAZURE_ENCODE_KEY = MYAZURE_ENCODE_KEY;
		MyazureConstants.MYAZURE_APP_URL = MYAZURE_APP_URL;
		MyazureConstants.MYAZURE_SERVER_ID = MYAZURE_SERVER_ID;
	}

	public void initTokenManager() {
		API.defaultMode(API.MODE_POPULAR);
		LocalHttpClient.init(50, 10);
	}

	public static void initWXBizMsgCrypt() {
		try {
			MyazureConstants.MYAZUZRE_WXBIZMSGCRYPT = new WXBizMsgCrypt(MyazureConstants.MYAZURE_ENCODE_TOKEN, MyazureConstants.MYAZURE_ENCODE_KEY,
					MyazureConstants.MYAZURE_APP_ID);
		} catch (AesException e) {
			LOG.error(e.getMessage());
		}
	}

	public static void contextDestroyed() {
		TokenManager.destroyed();
	}

	public boolean checkInit() {
		if (MyazureConstants.MYAZURE_APP_ID == null || MyazureConstants.MYAZURE_APP_SECRET == null || MyazureConstants.MYAZURE_ENCODE_TOKEN == null
				|| MyazureConstants.MYAZURE_ENCODE_KEY == null) {
			LOG.error("System Going Down|!!!!!!!!!!!!!!!!!!NULL!!!ID!!!!SECRET!!!!TOKEN!!!!!!KEY!!!!!");
			return false;
		}
		return true;
	}
}
