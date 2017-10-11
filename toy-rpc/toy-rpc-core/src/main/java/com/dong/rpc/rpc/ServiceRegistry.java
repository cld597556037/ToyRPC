package com.dong.rpc.rpc;

/**
 * 服务注册接口
 *
 * @author caolidong
 * @date 17/6/26.
 */
public interface ServiceRegistry extends Closeable {

    void register(String serviceName, String serviceAddress);

    void unregister(String serviceName, String serviceAddress);
}
