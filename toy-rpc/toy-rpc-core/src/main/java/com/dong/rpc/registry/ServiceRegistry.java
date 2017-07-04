package com.dong.rpc.registry;

/**
 * 服务注册接口
 * @author caolidong
 * @date 17/6/26.
 */
public interface ServiceRegistry {

    void register(String serviceName, String serviceAddress);
}
