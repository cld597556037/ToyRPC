package com.dong.rpc.spring;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author caolidong
 * @date 17/8/31.
 */
public class ConfigSpringServerTest {

    @Test
    public void test() {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-test-server-spring.xml");
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        context.close();
    }

}
