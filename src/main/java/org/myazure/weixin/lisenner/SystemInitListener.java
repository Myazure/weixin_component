package org.myazure.weixin.lisenner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.myazure.weixin.constant.MyazureConstants;
import org.myazure.weixin.constant.WeixinConstans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import com.qq.weixin.mp.aes.AesException;
import com.qq.weixin.mp.aes.WXBizMsgCrypt;

import weixin.popular.api.API;
import weixin.popular.client.LocalHttpClient;
import weixin.popular.support.TokenManager;

/**
 * Token 监听器
 * 
 * @author WangZhen
 *
 */
@Component("servletContextListener")
public class SystemInitListener implements ServletContextListener {
	private static final Logger LOG = LoggerFactory.getLogger(SystemInitListener.class);
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
	@Autowired
	private StringRedisTemplate redisTemplate;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		MyazureConstants.MYAZURE_APP_ID = MYAZURE_APP_ID;
		MyazureConstants.MYAZURE_APP_SECRET = MYAZURE_APP_SECRET;
		MyazureConstants.MYAZURE_ENCODE_TOKEN = MYAZURE_ENCODE_TOKEN;
		MyazureConstants.MYAZURE_ENCODE_KEY = MYAZURE_ENCODE_KEY;
		MyazureConstants.MYAZURE_APP_URL = MYAZURE_APP_URL;
		MyazureConstants.MYAZURE_SERVER_ID = MYAZURE_SERVER_ID;
		API.defaultMode(API.MODE_POPULAR);
		MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN = redisTemplate.opsForValue().get(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY);
		LocalHttpClient.init(1000, 100);
		try {
			MyazureConstants.MYAZUZRE_WXBIZMSGCRYPT = new WXBizMsgCrypt(MyazureConstants.MYAZURE_ENCODE_TOKEN, MyazureConstants.MYAZURE_ENCODE_KEY,
					MyazureConstants.MYAZURE_APP_ID);
		} catch (AesException e) {
			LOG.error(e.getMessage());
		}
		if (MyazureConstants.MYAZURE_APP_ID == null || MyazureConstants.MYAZURE_APP_SECRET == null || MyazureConstants.MYAZURE_ENCODE_TOKEN == null
				|| MyazureConstants.MYAZURE_ENCODE_KEY == null) {
			LOG.error("System Going Down|!!!!!!!!!!!!!!!!!!NULL!!!ID!!!!SECRET!!!!TOKEN!!!!!!KEY!!!!!");
		}
		LOG.debug("Myazure Weixin MYAZURE_COMPONENT_ACCESS_TOKEN:"+MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		TokenManager.destroyed();
	}
}