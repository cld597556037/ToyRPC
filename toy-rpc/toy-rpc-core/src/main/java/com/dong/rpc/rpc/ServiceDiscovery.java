package com.dong.rpc.rpc;

/**
 * 服务发现接口
 *
 * @author caolidong
 * @date 17/6/26.
 */
public interface ServiceDiscovery extends Closeable {

    /**
     * 订阅
     *
     * @param serviceName
     * @param cluster
     */
    void subscribe(String serviceName, Cluster cluster);

    /**
     * 取消订阅
     *
     * @param serviceName
     * @param cluster
     */
    void unsubscribe(String serviceName, Cluster cluster);
}
