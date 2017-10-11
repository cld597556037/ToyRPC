package com.dong.rpc.registry;

import com.dong.rpc.rpc.ServiceRegistry;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author caolidong
 * @date 17/7/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-test-registry.xml"})
public class ZKServiceRegistryTest {
    @Autowired
    private ServiceRegistry serviceRegistry;

    @Test
    public void testRegister() throws InterruptedException {
        serviceRegistry.register("helloService", "192.168.1.1:8889");
        Thread.sleep(20000);
        serviceRegistry.register("helloService", "192.168.1.1:9999");
        Thread.sleep(20000);
    }
}
