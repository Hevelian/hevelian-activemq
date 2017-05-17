package com.hevelian.activemq.xbean;

import java.net.MalformedURLException;

import org.apache.activemq.spring.Utils;
import org.apache.activemq.xbean.XBeanBrokerFactory;
import org.apache.xbean.spring.context.ResourceXmlApplicationContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.FatalBeanException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.Resource;
import org.springframework.web.context.support.StandardServletEnvironment;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hevelian.activemq.servlet.ServletContextHolder;

public class WebXBeanBrokerFactory extends XBeanBrokerFactory {
	private static final transient Logger LOG = LoggerFactory.getLogger(WebXBeanBrokerFactory.class);

	protected ApplicationContext createApplicationContext(String uri) throws MalformedURLException {
		Resource resource = Utils.resourceFromString(uri);
		LOG.debug("Using " + resource + " from " + uri);
		try {
			return new ResourceXmlApplicationContext(resource) {
				@Override
				protected ConfigurableEnvironment createEnvironment() {
					return new StandardServletEnvironment();
				}

				@Override
				protected void initPropertySources() {
					WebApplicationContextUtils.initServletPropertySources(getEnvironment().getPropertySources(),
							ServletContextHolder.getServletContext());
				}
			};
		} catch (FatalBeanException errorToLog) {
			LOG.error("Failed to load: " + resource + ", reason: " + errorToLog.getLocalizedMessage(), errorToLog);
			throw errorToLog;
		}
	}

}
