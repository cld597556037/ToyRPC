package com.dong.rpc.spring.chain;

import com.dong.rpc.exception.HelloException;
import com.dong.rpc.service.hello.HelloService;
import com.dong.rpc.service.name.NameService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

/**
 * @author caolidong
 * @date 17/9/1.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:application-test-client-chain-spring.xml"})
public class ConfigSpringClientTest {

    @Autowired
    public HelloService helloService;

    @Test
    public void test() {
        try {
            assertEquals(helloService.hello("dong"), "hello, dong");
        } catch (HelloException e) {
            e.printStackTrace();
        }
    }
}
