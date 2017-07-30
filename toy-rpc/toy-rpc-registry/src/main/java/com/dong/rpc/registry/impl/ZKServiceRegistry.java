package com.dong.rpc.registry.impl;

import com.dong.rpc.registry.ServiceRegistry;
import com.dong.rpc.util.Constant;
import com.dong.rpc.util.ZookeeperUtil;
import com.github.zkclient.ZkClient;
import com.sun.tools.internal.jxc.ap.Const;
import org.apache.log4j.Logger;
import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 基于Zookeeper的服务注册
 * @author caolidong
 * @date 17/6/26.
 */
public class ZKServiceRegistry implements ServiceRegistry {
    private static final Logger LOGGER = Logger.getLogger(ZKServiceRegistry.class);

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    private ZkClient zkClient;

    public ZKServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
        zkClient = new ZkClient(registryAddress);
    }

    /**
     * 此处加同步只是为了测试方便
     * @param serviceName
     * @param serviceAddress
     */
    public synchronized void register(String serviceName, String serviceAddress) {
        if (!zkClient.exists(Constant.ZK_REGISTRY_PATH)) {
            zkClient.createPersistent(Constant.ZK_REGISTRY_PATH);
        }

        if (!zkClient.exists(Constant.ZK_DATA_PATH)) {
            zkClient.createPersistent(Constant.ZK_DATA_PATH);
            LOGGER.info("create data node: " + Constant.ZK_DATA_PATH);
        }
        String servicePath = String.format("%s/%s", Constant.ZK_DATA_PATH, serviceName);
        if (!zkClient.exists(servicePath)) {
            zkClient.createPersistent(servicePath);
            LOGGER.info("create service node: " + servicePath);
        }

        String addressPath = String.format("%s/%s", servicePath, serviceAddress);
        zkClient.createEphemeral(addressPath, serviceAddress.getBytes());
        LOGGER.info("register address info :" + addressPath);
    }

}
