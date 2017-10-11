package com.dong.rpc.config;

import com.dong.rpc.config.api.ConsumerConfig;
import com.dong.rpc.config.api.DiscoveryConfig;
import com.dong.rpc.config.api.ProtocolConfig;
import com.dong.rpc.service.name.NameService;
import com.dong.rpc.task.ConsumerTask;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class ConfigClientTest {

    private static String ZK_URL = "zk://121.43.104.34:2181";
    private static String interfaceClass = "com.dong.rpc.service.name.NameService";

    private ConsumerConfig getConsumerConfig(String registryUrl, String clazz) {
        DiscoveryConfig discoveryConfig = new DiscoveryConfig();
        discoveryConfig.setUrl(registryUrl);

        ProtocolConfig protocolConfig = new ProtocolConfig();
        protocolConfig.setName("default");
        protocolConfig.setHaStrategy("failfast");
        protocolConfig.setLoadBalance("random");

        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.setDiscoveryConfig(discoveryConfig);
        consumerConfig.setProtocolConfig(protocolConfig);
        consumerConfig.setInterface(clazz);
        return consumerConfig;
    }

    @Test
    public void testConfigClient() {
        ConsumerConfig<NameService> consumerConfig = getConsumerConfig(ZK_URL, interfaceClass);
        NameService nameService = consumerConfig.refer();
        String name = nameService.getName();
        assertEquals("dong", name);
        try {
            consumerConfig.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConfigClientParallel() {
        ConsumerConfig<NameService> consumerConfig = getConsumerConfig(ZK_URL, interfaceClass);
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
        try {
            consumerConfig.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConsumerTaskWithOneConsumer() {
        ConsumerConfig<NameService> consumerConfig = getConsumerConfig(ZK_URL, interfaceClass);
        NameService nameService = consumerConfig.refer();
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(new ConsumerTask(nameService, i));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            consumerConfig.destroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConsumerTaskWithConsumers() {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        List<ConsumerConfig> configList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            ConsumerConfig<NameService> consumerConfig = getConsumerConfig(ZK_URL, interfaceClass);
            configList.add(consumerConfig);
            NameService nameService = consumerConfig.refer();
            executorService.submit(new ConsumerTask(nameService, i));
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(20, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        configList.stream().forEach(config -> {
            try {
                config.destroy();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
