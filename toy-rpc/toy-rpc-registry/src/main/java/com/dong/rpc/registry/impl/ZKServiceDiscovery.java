package com.dong.rpc.registry.impl;

import com.dong.rpc.extension.SpiMeta;
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
 * @author caolidong
 * @date 17/6/26.
 */
@SpiMeta(name = "zk")
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

    public void open(String registryAddress) {
        this.registryAddress = registryAddress;
        zkClient = new ZkClient(registryAddress);
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
        List<Cluster> clusters = clustersMap.get(serviceName);
        if (clusters == null) {
            clusters = new ArrayList<>();
            clustersMap.put(serviceName, clusters);
        }
        clusters.add(cluster);
        cluster.notify(discover(serviceName));
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
