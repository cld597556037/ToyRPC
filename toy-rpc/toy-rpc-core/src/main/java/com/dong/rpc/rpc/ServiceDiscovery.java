package com.dong.rpc.rpc;

import com.dong.rpc.extension.Spi;

import java.util.List;

/**
 * 服务发现接口
 * @author caolidong
 * @date 17/6/26.
 */
@Spi
public interface ServiceDiscovery {

    void open(String address);

    /**
     * 服务的可用地址列表
     * @param serviceName
     * @return
     */
    List<String> discover(String serviceName);

    /**
     *  关闭发现服务器的连接
     *  关闭Channel
     *  释放Channel对象池
     *
     */
    void close();

    void subscribe(String serviceName, Cluster cluster);
}
