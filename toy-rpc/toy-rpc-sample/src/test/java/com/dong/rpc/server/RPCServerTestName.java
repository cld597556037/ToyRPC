package com.dong.rpc.server;

import com.dong.rpc.registry.ServiceRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author caolidong
 * @date 17/6/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-test-server-name.xml"})
public class RPCServerTestName {

    @Autowired
    RPCServiceInitializer initializer;

    @Autowired
    ServiceRegistry zkServiceRegistry;

    @Test
    public void testServerWithZK() {
        RPCServer rpcServer = new RPCServerImpl(8763, zkServiceRegistry, initializer.getServiceMap());
        rpcServer.start();
    }

}
