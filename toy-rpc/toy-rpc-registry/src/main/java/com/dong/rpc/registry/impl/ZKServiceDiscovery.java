package com.dong.rpc.registry.impl;

import com.dong.rpc.rpc.Cluster;
import com.dong.rpc.rpc.ServiceDiscovery;
import com.dong.rpc.util.Constant;
import com.github.zkclient.ZkClient;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 基于Zookeeper的服务发现
 *
 * @author caolidong
 * @date 17/6/26.
 */
public class ZKServiceDiscovery implements ServiceDiscovery {

    private static final Logger LOGGER = Logger.getLogger(ZKServiceDiscovery.class);

    /**
     * zk地址
     */
    private String registryAddress;

    private ZkClient zkClient;

    /**
     * 服务地址
     */
    private Map<String, List<String>> serviceAddresses = new HashMap<>();

    private Map<String, List<Cluster>> clustersMap = new HashMap<>();

    public ZKServiceDiscovery(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    @Override
    public void open() {
        zkClient = new ZkClient(this.registryAddress);
    }

    public List<String> discover(String serviceName) {
        String servicePath = String.format("%s/%s", Constant.ZK_DATA_PATH, serviceName);
        List<String> addresses = zkClient.getChildren(servicePath);
        LOGGER.info(String.format("%s 服务地址: %s", serviceName, addresses));
        return addresses;
    }

    @Override
    public void subscribe(String serviceName, Cluster cluster) {
        String servicePath = String.format("%s/%s", Constant.ZK_DATA_PATH, serviceName);
        zkClient.subscribeChildChanges(servicePath, (path, children) -> {
            serviceAddresses.put(serviceName, children);
            update(serviceName);
        });
        clustersMap.putIfAbsent(serviceName, new ArrayList<>());
        List<Cluster> clusters = clustersMap.get(serviceName);
        clusters.add(cluster);
        cluster.notify(discover(serviceName));
    }

    @Override
    public void unsubscribe(String serviceName, Cluster cluster) {
        List<Cluster> clusters = clustersMap.get(serviceName);
        clusters.remove(cluster);
    }

    private void update(String serviceName) {
        List<Cluster> clusters = clustersMap.get(serviceName);
        clusters.stream().forEach(cluster -> cluster.notify(discover(serviceName)));
    }

    @Override
    public void close() {
        zkClient.close();
    }
}
