package com.dong.rpc.server;

import com.dong.rpc.registry.ServiceRegistry;
import com.dong.rpc.registry.impl.LocalServiceRegistry;
import org.junit.Test;
import com.dong.rpc.service.hello.HelloService;
import com.dong.rpc.service.hello.HelloServiceImpl;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * @author caolidong
 * @date 17/6/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-test-server-hello.xml"})
public class RPCServerTest {

    @Autowired
    RPCServiceInitializer initializer;

    @Autowired
    ServiceRegistry zkServiceRegistry;

    @Test
    public void serverStartTest() {
        Map<String, Object> services = new HashMap<>();
        services.put(HelloService.class.getName(), new HelloServiceImpl());
        RPCServer server = new RPCServerImpl(8766, new LocalServiceRegistry(), services);
        server.start();
    }

    @Test
    public void testParallelServer() throws InterruptedException, ExecutionException {
        ServiceRegistry registry = new LocalServiceRegistry();
        Map<String, Object> services = new HashMap<>();
        services.put(HelloService.class.getName(), new HelloServiceImpl());

        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Callable<Object>> callables = new ArrayList<>();
        callables.add(Executors.callable(new ServerTask(8765, registry, services)));
        callables.add(Executors.callable(new ServerTask(8766, registry, services)));
        executor.invokeAll(callables);
    }

    @Test
    public void testServerWithZK() {
        RPCServer rpcServer = new RPCServerImpl(8765, zkServiceRegistry, initializer.getServiceMap());
        rpcServer.start();
    }

    @Test
    public void testServersWithZK() throws InterruptedException, ExecutionException  {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Callable<Object>> callables = new ArrayList<>();
        callables.add(Executors.callable(new ServerTask(8765, zkServiceRegistry, initializer.getServiceMap())));
        callables.add(Executors.callable(new ServerTask(8766, zkServiceRegistry, initializer.getServiceMap())));
        executor.invokeAll(callables);
    }

}
