package com.dong.rpc.config.api;

import com.dong.rpc.util.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务提供Config
 * @author caolidong
 * @date 17/8/30.
 */
public class ProviderConfig {

    private Logger logger = LoggerFactory.getLogger(ProviderConfig.class);

    private String interfaceClass;

    private Object reference;

    private ProtocolConfig protocolConfig;

    private RegistryConfig registryConfig;

    private ServerConfig serverConfig;

    public String getInterface() {
        return interfaceClass;
    }

    public void setInterface(String interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public Object getReference() {
        return reference;
    }

    public void setReference(Object reference) {
        this.reference = reference;
    }

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    public ProtocolConfig getProtocolConfig() {
        return protocolConfig;
    }

    public void setProtocolConfig(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
    }

    public RegistryConfig getRegistryConfig() {
        return registryConfig;
    }

    public void setRegistryConfig(RegistryConfig registryConfig) {
        this.registryConfig = registryConfig;
    }

    public void export() {
        logger.info("export provider:" + this);
        try {
            Class clazz = (Class) Class.forName(interfaceClass);
            this.protocolConfig.getProtocol().export(clazz, reference, serverConfig.getPort());
            this.registryConfig.getServiceRegistry().register(interfaceClass,
                    NetUtil.getLocalIP() + ":" + serverConfig.getPort());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ProviderConfig [" +
                "interfaceClass='" + interfaceClass + '\'' +
                ", reference=" + reference +
                "]";
    }
}
