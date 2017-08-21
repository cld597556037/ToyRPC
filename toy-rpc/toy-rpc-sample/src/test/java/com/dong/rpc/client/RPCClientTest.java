package com.dong.rpc.client;

import com.dong.rpc.registry.ServiceDiscovery;
import org.junit.Before;
import org.junit.Test;
import com.dong.rpc.service.hello.HelloService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;


/**
 * @author caolidong
 * @date 17/6/29.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-test-client-hello.xml"})
public class RPCClientTest {

    @Autowired
    private HelloService helloService;

    @Test
    public void clientNormalTest() {
        try {
            String hello = helloService.hello("dong");
            assertEquals(hello, "hello, dong");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void clientExceptionTest() {
        try {
            helloService.hello("exception");
            assert false;
        } catch (Exception e) {
            assertEquals("hello", e.getMessage());
        }

    }

    @Test
    public void clientLoopTest() {
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
        Executor executor = Executors.newFixedThreadPool(10);
        try {
            for (int i = 0; i < 10; i++) {
                executor.execute(new ClientTask(helloService, i + 1));

                    Thread.sleep(200);

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void clientAsyncTest() {
        try {
            helloService.async("dong");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
