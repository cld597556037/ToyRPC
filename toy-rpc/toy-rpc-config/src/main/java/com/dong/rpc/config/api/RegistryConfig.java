package com.dong.rpc.config.api;

import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.ExtensionLoader;
import com.dong.rpc.rpc.RegistryFactory;
import com.dong.rpc.rpc.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务注册Config
 *
 * @author caolidong
 * @date 17/8/30.
 */
public class RegistryConfig {

    private Logger logger = LoggerFactory.getLogger(RegistryConfig.class);

    private String url;

    private ServiceRegistry serviceRegistry;

    public ServiceRegistry getServiceRegistry() {
        if (serviceRegistry == null) {
            logger.info("初始化ServiceRegistry");
            String[] splits = url.split(ConfigConstant.URL_SPLIT);
            String type = splits[0];
            String address = splits[1];
            try {
                RegistryFactory factory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(type);
                serviceRegistry = factory.createRegistry(address);
                serviceRegistry.open();
            } catch (ToyException e) {
                e.printStackTrace();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(()-> {
                if (logger.isInfoEnabled()) {
                    logger.info("Run serviceRegistry shutdown hook now:" + serviceRegistry.toString());
                }
                serviceRegistry.close();
            }, serviceRegistry.toString()));
        }
        return serviceRegistry;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
