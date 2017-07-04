package com.dong.rpc.registry.impl;

import com.dong.rpc.registry.ServiceDiscovery;
import com.sun.deploy.util.ArrayUtil;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

/**
 * @author caolidong
 * @date 17/6/29.
 */
public class LocalServiceDiscovery implements ServiceDiscovery {

    @Override
    public List<String> disover(String serviceName) {
        return Arrays.asList("localhost:8765");
    }
}
