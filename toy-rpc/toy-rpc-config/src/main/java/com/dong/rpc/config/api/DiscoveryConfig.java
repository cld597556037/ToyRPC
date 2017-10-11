package com.dong.rpc.config.api;

import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.ExtensionLoader;
import com.dong.rpc.rpc.RegistryFactory;
import com.dong.rpc.rpc.ServiceDiscovery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务发现Config
 *
 * @author caolidong
 * @date 17/8/30.
 */
public class DiscoveryConfig {

    private Logger logger = LoggerFactory.getLogger(DiscoveryConfig.class);

    private String url;

    private ServiceDiscovery serviceDisovery;

    public ServiceDiscovery getServiceDisovery() {
        if (serviceDisovery == null) {
            String[] splits = url.split(ConfigConstant.URL_SPLIT);
            String type = splits[0];
            String address = splits[1];
            try {
                RegistryFactory factory = ExtensionLoader.getExtensionLoader(RegistryFactory.class).getExtension(type);
                serviceDisovery = factory.createDiscovery(address);
                serviceDisovery.open();
            } catch (ToyException e) {
                e.printStackTrace();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(()-> {
                if (logger.isInfoEnabled()) {
                    logger.info("Run serviceDisovery shutdown hook now:" + serviceDisovery.toString());
                }
                serviceDisovery.close();
            }, serviceDisovery.toString()));
        }
        return serviceDisovery;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
