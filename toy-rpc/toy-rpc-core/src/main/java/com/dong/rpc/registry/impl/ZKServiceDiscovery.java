package com.dong.rpc.registry.impl;

import com.dong.rpc.Constant;
import com.dong.rpc.registry.ServiceDiscovery;
import com.dong.rpc.util.ZookeeperUtil;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

/**
 * 基于Zookeeper的服务发现
 * @author caolidong
 * @date 17/6/26.
 */
public class ZKServiceDiscovery implements ServiceDiscovery {

    private CountDownLatch latch = new CountDownLatch(1);

    private String registryAddress;

    private ZooKeeper zk;

    private List<String> addressList;

    public ZKServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    @Override
    public List<String> disover(String serviceName) {
        if (zk == null) {
            zk = ZookeeperUtil.connect(registryAddress, latch);
            watchNode(zk);
        }

        return addressList.stream().filter(item -> item.equals(serviceName)).collect(Collectors.toList());
    }

    private void watchNode(final ZooKeeper zk) {
        try {
            List<String> nodeList = zk.getChildren(Constant.ZK_REGISTRY_PATH, event -> {
                        if (event.getType() == Watcher.Event.EventType.NodeChildrenChanged) {
                            watchNode(zk);
                        }});

            addressList = nodeList.stream().map(item -> {
                    byte[] bytes = null;
                    try {
                        bytes = zk.getData(Constant.ZK_REGISTRY_PATH + "/" + item, false, null);
                    } catch (KeeperException | InterruptedException e) {
                        e.printStackTrace();
                    }
                    return new String(bytes);
                }).collect(Collectors.toList());
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
