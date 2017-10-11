package com.dong.rpc.config.spring;

import com.dong.rpc.config.api.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class ServerBean extends ServerConfig implements InitializingBean, DisposableBean, BeanNameAware, ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(ServerBean.class);

    protected ApplicationContext applicationContext = null;

    @Override
    public void setBeanName(String s) {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
