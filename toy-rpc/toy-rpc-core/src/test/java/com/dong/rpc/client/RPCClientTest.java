package com.dong.rpc.client;

import com.dong.rpc.registry.impl.LocalServiceDiscovery;
import com.dong.rpc.registry.ServiceDiscovery;
import com.dong.rpc.service.HelloService;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;


/**
 * @author caolidong
 * @date 17/6/29.
 */
public class RPCClientTest {

    private RPCProxy rpcProxy;

    @Before
    public void init() {
        ServiceDiscovery serviceDiscovery = new LocalServiceDiscovery();
        RPCClient rpcClient = new RPCClient(serviceDiscovery);
        rpcProxy = new RPCProxy(rpcClient);
    }

    @Test
    public void clientNormalTest() {
        HelloService helloService  = rpcProxy.create(HelloService.class);
        try {
            String hello = helloService.hello("dong");
            assertEquals(hello, "hello, dong");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void clientExceptionTest() {
        HelloService helloService  = rpcProxy.create(HelloService.class);
        try {
            helloService.hello("exception");
            assert false;
        } catch (Exception e) {
            assertEquals("hello", e.getMessage());
        }

    }

    @Test
    public void clientLoopTest() {
        HelloService helloService  = rpcProxy.create(HelloService.class);
        try {
            for (int i = 0; i < 10; i++) {
                String hello = helloService.hello("dong" + i);
                assertEquals(hello, "hello, dong" + i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void clientParallelTest() {
        HelloService helloService  = rpcProxy.create(HelloService.class);

        Executor executor = Executors.newFixedThreadPool(10);
        try {
            for (int i = 0; i < 10; i++) {
                executor.execute(new ClientTask(helloService, i + 1));

                    Thread.sleep(100);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
