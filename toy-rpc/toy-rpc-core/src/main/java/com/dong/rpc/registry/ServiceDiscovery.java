package com.dong.rpc.registry;

import java.util.List;

/**
 * 服务发现接口
 * @author caolidong
 * @date 17/6/26.
 */
public interface ServiceDiscovery {

    List<String> disover(String serviceName);

}
