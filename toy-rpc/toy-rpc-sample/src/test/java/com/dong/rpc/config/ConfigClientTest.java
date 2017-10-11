package com.dong.rpc.config;

import com.dong.rpc.config.api.ConsumerConfig;
import com.dong.rpc.config.api.DiscoveryConfig;
import com.dong.rpc.config.api.ProtocolConfig;
import com.dong.rpc.service.name.NameService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class ConfigClientTest {

    private DiscoveryConfig discoveryConfig;

    private ProtocolConfig protocolConfig;

    private ConsumerConfig<NameService> consumerConfig;

    @Before
    public void init(){
        discoveryConfig = new DiscoveryConfig();
        discoveryConfig.setUrl("zk://121.43.104.34:2181");

        protocolConfig = new ProtocolConfig();
        protocolConfig.setName("default");

        consumerConfig = new ConsumerConfig();
        consumerConfig.setDiscoveryConfig(discoveryConfig);
        consumerConfig.setProtocolConfig(protocolConfig);
        consumerConfig.setInterface("com.dong.rpc.service.name.NameService");
    }

    @Test
    public void testConfigClient() {
        NameService nameService = consumerConfig.refer();
        String name = nameService.getName();
        assertEquals("dong", name);
    }

    @Test
    public void testConfigClientParallel() {
        NameService nameService = consumerConfig.refer();
        String name = nameService.getName();
        assertEquals("dong", name);
        try {
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        name = nameService.getName();
        assertEquals("dong", name);
    }
}
