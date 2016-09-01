package org.myazure.weixin.handlers;

import org.myazure.weixin.constant.MyazureConstants;
import org.myazure.weixin.constant.WeixinConstans;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import weixin.popular.bean.component.ComponentReceiveXML;

public class VerifyTicketHandler {
	private static final Logger LOG = LoggerFactory.getLogger(VerifyTicketHandler.class);
	@Autowired
	private static   StringRedisTemplate redisTemplate;
	
	/**
	 * 刷新凭证
	 * 
	 * @param eventMessage
	 */
	public static void refreshVerifyTicket(ComponentReceiveXML eventMessage) {
		// Update component verify ticket in memory
		MyazureConstants.MYAZURE_COMPONENT_VERIFY_TICKET = eventMessage.getComponentVerifyTicket();
		// Refresh component access token
		LOG.info("[Myazure Weixin]: Refresh component access token from redis.");
		String accessToken = redisTemplate.opsForValue().get(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY);
		if (null == accessToken || accessToken.trim().length() == 0) {
			MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN = AuthorizeHandler.getComponentAccessTokenStr();
		} else if (redisTemplate.getExpire(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY) < 1000) {
			MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN = AuthorizeHandler.getComponentAccessTokenStr();
		}
		if (MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN == null) {
			MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN = redisTemplate.opsForValue().get(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY);
		}
		LOG.debug("[Myazure Weixin]: COMPONENT VIRFY TIKET NOW = {}", MyazureConstants.MYAZURE_COMPONENT_VERIFY_TICKET);
		LOG.debug("[Myazure Weixin]: Myazure COMPONENT ACCESS TOKEN NOW = {}", MyazureConstants.MYAZURE_COMPONENT_ACCESS_TOKEN);
		LOG.debug("[Myazure Weixin]: COMPONENT ACCESS TOKEN TIME= {}", redisTemplate.getExpire(WeixinConstans.COMPONENT_ACCESS_TOKEN_KEY));
	}
}
