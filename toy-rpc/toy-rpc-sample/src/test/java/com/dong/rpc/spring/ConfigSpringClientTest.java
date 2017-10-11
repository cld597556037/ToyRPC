package com.dong.rpc.spring;

import com.dong.rpc.service.name.NameService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import static org.junit.Assert.assertEquals;

/**
 * @author caolidong
 * @date 17/9/1.
 */
public class ConfigSpringClientTest {

    @Autowired
    public NameService nameService;

    @Test
    public void test() {
        ConfigurableApplicationContext context = new ClassPathXmlApplicationContext("classpath:application-test-client-spring.xml");
        nameService = context.getBean(NameService.class);
        assertEquals(nameService.getName(), "dong");
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        context.close();
    }
}
