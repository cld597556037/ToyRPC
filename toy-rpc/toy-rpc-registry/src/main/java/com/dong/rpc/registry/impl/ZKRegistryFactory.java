package com.dong.rpc.registry.impl;

import com.dong.rpc.extension.SpiMeta;
import com.dong.rpc.rpc.RegistryFactory;
import com.dong.rpc.rpc.ServiceDiscovery;
import com.dong.rpc.rpc.ServiceRegistry;

/**
 * @author caolidong
 * @date 2017/10/13.
 */
@SpiMeta(name = "zk")
public class ZKRegistryFactory implements RegistryFactory {
    @Override
    public ServiceRegistry createRegistry(String address) {
        ServiceRegistry serviceRegistry = new ZKServiceRegistry(address);
        return serviceRegistry;
    }

    @Override
    public ServiceDiscovery createDiscovery(String address) {
        ServiceDiscovery serviceDiscovery = new ZKServiceDiscovery(address);
        return serviceDiscovery;
    }
}
