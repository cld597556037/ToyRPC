package com.dong.rpc.config.spring;

import com.dong.rpc.config.api.ProtocolConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author caolidong
 * @date 2017/10/12.
 */
public class ProtocolBean extends ProtocolConfig implements InitializingBean, DisposableBean, BeanNameAware {

    private Logger logger = LoggerFactory.getLogger(ProtocolBean.class);

    @Override
    public void setBeanName(String s) {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
