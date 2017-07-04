package com.dong.rpc.registry.impl;

import com.dong.rpc.Constant;
import com.dong.rpc.registry.ServiceRegistry;
import com.dong.rpc.util.ZookeeperUtil;
import org.apache.zookeeper.*;

import java.util.concurrent.CountDownLatch;

/**
 * 基于Zookeeper的服务注册
 * @author caolidong
 * @date 17/6/26.
 */
public class ZKServiceRegistry implements ServiceRegistry {

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    private ZooKeeper zk;

    public ZKServiceRegistry(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void register(String serviceName, String serviceAddress) {
        if (zk == null) {
            zk = ZookeeperUtil.connect(registryAddress, latch);
        }
        if (serviceAddress != null) {
            createNode(zk, serviceName, serviceAddress);
        }
    }

    private void createNode(ZooKeeper zk, String serviceName, String serviceAddress) {
        byte[] bytes = serviceAddress.getBytes();
        try {
            String path = zk.create(Constant.ZK_DATA_PATH + "/" + serviceName, bytes,
                    ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        } catch (KeeperException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
