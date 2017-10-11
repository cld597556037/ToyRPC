package com.dong.rpc.spring.chain;

import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class ConfigSpringClientServerTest {

    @Test
    public void test() {
        new ClassPathXmlApplicationContext("classpath:application-test-client-server-chain-spring.xml");
        try {
            Thread.sleep(800000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
