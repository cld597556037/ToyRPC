package com.dong.rpc.config;

import com.dong.rpc.config.api.ProtocolConfig;
import com.dong.rpc.config.api.ProviderConfig;
import com.dong.rpc.config.api.RegistryConfig;
import com.dong.rpc.config.api.ServerConfig;
import com.dong.rpc.service.name.NameServiceImpl;
import org.junit.Test;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class ConfigServerTest {

    private ProviderConfig getProviderConfig(String registruUrl, int port, String clazz) {
        RegistryConfig registryConfig = new RegistryConfig();
        registryConfig.setUrl(registruUrl);

        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setPort(port);

        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("default");

        ProviderConfig providerConfig = new ProviderConfig();
        providerConfig.setServerConfig(serverConfig);
        providerConfig.setProtocolConfig(protocolConfig);
        providerConfig.setRegistryConfig(registryConfig);
        providerConfig.setInterface(clazz);
        providerConfig.setReference(new NameServiceImpl());
        return providerConfig;
    }

    @Test
    public void testServerConfig() {
        ProviderConfig providerConfig = getProviderConfig("zk://121.43.104.34:2181", 11367,
                "com.dong.rpc.service.name.NameService");
        providerConfig.export();
        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            providerConfig.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testServerConfigParallel() {
        ProviderConfig providerConfig = getProviderConfig("zk://121.43.104.34:2181", 11367,
                "com.dong.rpc.service.name.NameService");
        providerConfig.export();
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ProviderConfig providerConfig1 = getProviderConfig("zk://121.43.104.34:2181", 11368,
                "com.dong.rpc.service.name.NameService");
        providerConfig1.export();
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            providerConfig.destroy();
            providerConfig1.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
