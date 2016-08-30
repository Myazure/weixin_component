package org.myazure.weixin.lisenner;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.myazure.weixin.initialize.ConstantInitialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Token 监听器
 * 
 * @author WangZhen
 *
 */
public class SystemInitListener implements ServletContextListener {
	private static final Logger LOG = LoggerFactory.getLogger(SystemInitListener.class);
	ConstantInitialize initializer = new ConstantInitialize();

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		LOG.debug("System Init Start~");
		initializer.init();
		LOG.debug("System ReaDy~");
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ConstantInitialize.contextDestroyed();
	}
}