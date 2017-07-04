package com.dong.rpc.server;

import com.dong.rpc.registry.impl.LocalServiceRegistry;
import com.dong.rpc.service.HelloService;
import com.dong.rpc.service.HelloServiceImpl;
import org.junit.Test;

/**
 * @author caolidong
 * @date 17/6/29.
 */

public class RPCServerTest {

    @Test
    public void serverStartTest() {
        RPCServer server = new RPCServerImpl(8765, new HelloServiceImpl(), HelloService.class.getName(),
                new LocalServiceRegistry());
        server.start();
    }
}
