package com.dong.rpc.task;

import com.dong.rpc.rpc.ServiceRegistry;

import java.util.Map;

/**
 * @author caolidong
 * @date 17/7/21.
 */
public class ServerTask implements Runnable {

    private int port;

    private ServiceRegistry registry;

    Map<String, Object> services;

    public ServerTask(int port, ServiceRegistry registry, Map<String, Object> services) {
        this.port = port;
        this.registry = registry;
        this.services = services;
    }

    @Override
    public void run() {
    }
}
