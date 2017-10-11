package com.dong.rpc.service.hello;

import com.dong.rpc.exception.HelloException;
import com.dong.rpc.service.name.NameService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author caolidong
 * @date 17/6/29.
 */
public class HelloServiceImpl implements HelloService {

    @Autowired
    private NameService nameService;

    @Override
    public String hello(String name) throws HelloException {
        if (name.equals("exception")) {
            throw new HelloException("hello");
        }
        return "hello, " + nameService.getName();
    }

}
