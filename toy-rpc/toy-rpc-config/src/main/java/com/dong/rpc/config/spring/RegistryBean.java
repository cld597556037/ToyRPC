package com.dong.rpc.config.spring;

import com.dong.rpc.config.api.RegistryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class RegistryBean extends RegistryConfig implements InitializingBean, DisposableBean, BeanNameAware {

    private Logger logger = LoggerFactory.getLogger(RegistryBean.class);

    @Override
    public void setBeanName(String name) {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
