package com.dong.rpc.registry;

import com.dong.rpc.rpc.ServiceDiscovery;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author caolidong
 * @date 17/7/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-test-discover.xml"})
public class ZKServiceDiscoveryTest {

    @Autowired
    private ServiceDiscovery serviceDiscovery;

    @Test
    public void testDiscover() throws InterruptedException {
        List<String> addresses = serviceDiscovery.discover("helloService");
        Thread.sleep(300000);
//        assertNotNull(addresses);
    }

}
