package com.dong.rpc.config.spring;

import com.dong.rpc.config.api.ProtocolConfig;
import com.dong.rpc.config.api.ProviderConfig;
import com.dong.rpc.config.api.RegistryConfig;
import com.dong.rpc.config.api.ServerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class ProviderBean extends ProviderConfig implements InitializingBean, DisposableBean, BeanNameAware, ApplicationContextAware {

    private Logger logger = LoggerFactory.getLogger(ProviderBean.class);

    private ApplicationContext applicationContext;

    @Override
    public void setBeanName(String s) {

    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (getProtocolConfig() == null) {
            Map<String, ProtocolConfig> map = applicationContext.getBeansOfType(ProtocolConfig.class, false, false);
            ProtocolConfig protocolConfig = null;
            logger.info("ProviderBean 初始化 protocolConfig");
            if (map.size() == 0) {
                protocolConfig = new ProtocolConfig();
            } else {
                protocolConfig = map.entrySet().iterator().next().getValue();
            }
            this.setProtocolConfig(protocolConfig);
        }

        if (getRegistryConfig() == null) {
            Map<String, RegistryConfig> map = applicationContext.getBeansOfType(RegistryConfig.class, false, false);
            if (map.size() == 0) {
                logger.error("could not find any instance of Class RegistryConfig ");
            } else {
                this.setRegistryConfig(map.entrySet().iterator().next().getValue());
            }
        }


        if (getServerConfig() == null) {
            Map<String, ServerConfig> map = applicationContext.getBeansOfType(ServerConfig.class, false, false);
            if (map.size() < 1) {
                logger.error("could not find any instance of Class ServerConfig ");
            } else {
                this.setServerConfig(map.entrySet().iterator().next().getValue());
            }
        }
        this.export();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
