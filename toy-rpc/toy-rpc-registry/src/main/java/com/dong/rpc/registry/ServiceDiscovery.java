package com.dong.rpc.registry;

import com.dong.rpc.channel.ChannelWrapper;

import java.util.List;

/**
 * 服务发现接口
 * @author caolidong
 * @date 17/6/26.
 */
public interface ServiceDiscovery {

    /**
     * 服务的可用地址列表
     * @param serviceName
     * @return
     */
    List<String> discover(String serviceName);

    /**
     * 服务的可用ChannelWrapper
     * 发现注册模块实现选择功能
     * @param serviceName
     * @return
     */
    ChannelWrapper getChannelWrapper(String serviceName);

    /**
     *  关闭发现服务器的连接
     *  关闭Channel
     *  释放Channel对象池
     *
     */
    void close();
}
