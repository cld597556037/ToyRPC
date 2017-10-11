package com.dong.rpc.rpc;

import com.dong.rpc.extension.Spi;

/**
 * @author caolidong
 * @date 2017/10/13.
 */
@Spi
public interface RegistryFactory {

    ServiceRegistry createRegistry(String address);

    ServiceDiscovery createDiscovery(String address);
}
