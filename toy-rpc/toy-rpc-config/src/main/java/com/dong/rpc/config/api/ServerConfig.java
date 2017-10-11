package com.dong.rpc.config.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端Config
 * @author caolidong
 * @date 17/8/30.
 */
public class ServerConfig {

    private Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    private int port;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}
