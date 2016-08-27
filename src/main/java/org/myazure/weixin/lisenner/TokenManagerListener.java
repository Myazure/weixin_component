package org.myazure.weixin.lisenner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.myazure.weixin.service.AdsenseAPI;
import org.myazure.weixin.service.BASE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import weixin.popular.api.API;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.support.TokenManager;

/**
 * Token 监听器
 * 
 * @author WangZhen
 *
 */
@Component("tokenManagerListener")
public class TokenManagerListener implements ServletContextListener {
	private static final Logger LOG = LoggerFactory
			.getLogger(TokenManagerListener.class);

	@Autowired
	private AdsenseAPI adsenseAPI;
	
	@Value("${weixin.compAppId}")
	protected String YSW_APP_ID;
	@Value("${weixin.compAppSecret}")
	protected String YSW_APP_SECRET;
	@Value("${weixin.encode.token}")
	protected String YSW_ENCODE_TOKEN;
	@Value("${weixin.encode.key}")
	protected String YSW_ENCODE_KEY;
	@Value("${adsense.appUrl}")
	protected String YSW_APP_URL;
	@Value("${adsense.ipUrl}")
	protected String YSW_IP_URL;
	@Value("${adsense.serverId}")
	protected String SERVER_ID;
	

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		BASE.YSW_APP_ID = YSW_APP_ID;
		BASE.YSW_APP_SECRET = YSW_APP_SECRET;
		BASE.YSW_ENCODE_TOKEN = YSW_ENCODE_TOKEN;
		BASE.YSW_ENCODE_KEY = YSW_ENCODE_KEY;
		BASE.YSW_APP_URL = YSW_APP_URL;
		BASE.YSW_IP_URL = YSW_IP_URL;
		BASE.SERVER_ID = SERVER_ID;
		
		if (BASE.YSW_APP_ID == null || BASE.YSW_APP_SECRET == null) {
			LOG.error("System Going Down|!!!!!!!!!!!!!!!!!!NULL!!!ID!!!!SECRET!!!!!!!!!!!!!!!");
			System.exit(911);
		}
		API.defaultMode(API.MODE_POPULAR);
		LocalHttpClient.init(1000, 100);
		BASE.initWXBizMsgCrypt();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		TokenManager.destroyed();
	}
}