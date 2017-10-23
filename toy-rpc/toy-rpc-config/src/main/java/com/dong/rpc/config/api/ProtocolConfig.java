package com.dong.rpc.config.api;

import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.ExtensionLoader;
import com.dong.rpc.rpc.Protocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author caolidong
 * @date 2017/10/11.
 */
public class ProtocolConfig {

    private static Logger logger = LoggerFactory.getLogger(ProtocolConfig.class);

    private Protocol protocol;

    private String name;

    private String haStrategy;

    private String loadBalance;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHaStrategy() {
        return haStrategy;
    }

    public void setHaStrategy(String haStrategy) {
        this.haStrategy = haStrategy;
    }

    public String getLoadBalance() {
        return loadBalance;
    }

    public void setLoadBalance(String loadBalance) {
        this.loadBalance = loadBalance;
    }

    public Protocol getProtocol() {
        if (protocol == null) {
            try {
                protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension(name);
            } catch (ToyException e) {
                e.printStackTrace();
            }
            Runtime.getRuntime().addShutdownHook(new Thread(()-> {
                if (logger.isInfoEnabled()) {
                    logger.info("Run protocol shutdown hook now:" + protocol.toString());
                }
                protocol.destroy();
            }, protocol.toString()));
        }
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
