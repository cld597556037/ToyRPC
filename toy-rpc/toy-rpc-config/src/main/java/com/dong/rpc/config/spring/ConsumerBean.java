package com.dong.rpc.config.spring;

import com.dong.rpc.config.api.ConsumerConfig;
import com.dong.rpc.config.api.DiscoveryConfig;
import com.dong.rpc.config.api.ProtocolConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class ConsumerBean<T> extends ConsumerConfig<T> implements FactoryBean<T>, InitializingBean, DisposableBean, ApplicationContextAware, BeanNameAware {

    private Logger logger = LoggerFactory.getLogger(ConsumerBean.class);

    private ApplicationContext applicationContext;

    private String beanName;

    @Override
    public void setBeanName(String name) {
        this.beanName = name;
    }

    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (getProtocolConfig() == null) {
            Map<String, ProtocolConfig> map = applicationContext.getBeansOfType(ProtocolConfig.class, false, false);
            if (map.size() == 0) {
                logger.error("could not find any instance of Class ProtocolConfig ");
            } else {
                this.setProtocolConfig(map.entrySet().iterator().next().getValue());
            }
        }

        if (getDiscoveryConfig() == null) {
            Map<String, DiscoveryConfig> map = applicationContext.getBeansOfType(DiscoveryConfig.class, false, false);
            if (map.size() == 0) {
                logger.error("could not find any instance of Class DiscoveryConfig ");
            } else {
                this.setDiscoveryConfig(map.entrySet().iterator().next().getValue());
            }
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public T getObject() throws Exception {
        T bean = refer();
        logger.info("ConsumerBean getObject: " + getInterface());
        return bean;
    }

    @Override
    public Class<?> getObjectType() {
        if (StringUtils.isNotEmpty(this.getInterface())) {
            try {
                return Class.forName(this.getInterface());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
