package com.dong.rpc.registry.impl;

import com.dong.rpc.channel.ChannelWrapper;
import com.dong.rpc.registry.ServiceDiscovery;
import com.dong.rpc.util.Constant;
import com.github.zkclient.ZkClient;
import org.apache.log4j.Logger;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * 基于Zookeeper的服务发现
 * @author caolidong
 * @date 17/6/26.
 */
public class ZKServiceDiscovery implements ServiceDiscovery {

    private static final Logger LOGGER = Logger.getLogger(ZKServiceDiscovery.class);

    /**
     * zk地址
     */
    private String registryAddress;

    /**
     * 需要监听的服务
     */
    private List<String> serviceNames;

    private ZkClient zkClient;

    /**
     * 服务地址
     */
    private Map<String, List<String>> serviceAddresses;

    /**
     * ip:port, ChannelWrapper集合
     */
    private Map<String, ChannelWrapper> channelWrappers;

    public ZKServiceDiscovery(String registryAddress, List<String> serviceNames) {
        this.registryAddress = registryAddress;
        this.serviceNames = serviceNames;
    }

    @PostConstruct
    private void init() {
        zkClient = new ZkClient(registryAddress);
        serviceAddresses = new ConcurrentHashMap<>();
        channelWrappers = new ConcurrentHashMap<>();
        //初始化
        serviceNames.stream().forEach(serviceName -> {
            List<String> addresses = getAddresses(serviceName);
            serviceAddresses.put(serviceName, addresses);
            LOGGER.info(String.format("%s 服务初始化地址：%s", serviceName, addresses));
            //侦听节点变化
            zkClient.subscribeChildChanges(Constant.ZK_DATA_PATH + "/" + serviceName, (path, children) -> {
                LOGGER.info(String.format("%s 服务地址发生变化: %s", path, children));
                serviceAddresses.put(serviceName, children);
                updateChannelWrappers();
            });
        });
        updateChannelWrappers();
    }

    private List<String>  getAddresses(String serviceName) {
        String servicePath = String.format("%s/%s", Constant.ZK_DATA_PATH, serviceName);
        List<String> addresses = zkClient.getChildren(servicePath);
        return addresses;
    }

    public List<String> discover(String serviceName) {
        List<String> addresses = getAddresses(serviceName);
        LOGGER.info(String.format("%s 服务地址: %s", serviceName, addresses));
        return addresses;
    }

    @Override
    public ChannelWrapper getChannelWrapper(String serviceName) {
        List<String> addresses = serviceAddresses.get(serviceName);
        if (addresses == null || addresses.size() == 0) {
            return  null;
        }
        return channelWrappers.get(addresses.get(ThreadLocalRandom.current().nextInt(addresses.size())));
    }

    /**
     * 更新ChannelWrappers
     * 添加新address, 删除无效address
     */
    private synchronized void updateChannelWrappers() {
        Map<String, ChannelWrapper> newChannelWrappers = new ConcurrentHashMap<>();
        List<String> addresses = new ArrayList<>();
        serviceAddresses.entrySet().stream().forEach(entry -> {
            entry.getValue().stream().forEach(address -> {
                addresses.add(address);
            });
        });
        List<String> distinctAddresses = addresses.stream().distinct().collect(Collectors.toList());
        distinctAddresses.stream().forEach(address -> {
            if (channelWrappers.containsKey(address)) {
                newChannelWrappers.put(address, channelWrappers.get(address));
            } else {
                String[] splits = address.split(":");
                newChannelWrappers.put(address, new ChannelWrapper(splits[0], Integer.parseInt(splits[1])));

            }
        });
        channelWrappers = newChannelWrappers;
    }

    @Override
    public void close() {

    }
}
