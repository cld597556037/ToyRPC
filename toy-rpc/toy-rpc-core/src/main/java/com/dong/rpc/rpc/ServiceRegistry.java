package com.dong.rpc.rpc;

import com.dong.rpc.extension.Spi;

/**
 * 服务注册接口
 * @author caolidong
 * @date 17/6/26.
 */
@Spi
public interface ServiceRegistry {

    void open(String address);

    void register(String serviceName, String serviceAddress);

    void close();
}
