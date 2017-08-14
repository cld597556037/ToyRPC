package com.dong.rpc.service;

import com.dong.rpc.annotation.RPCService;
import com.dong.rpc.client.annotation.Async;
import com.dong.rpc.exception.HelloException;

/**
 * @author caolidong
 * @date 17/6/29.
 */
@RPCService()
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) throws HelloException {
        if (name.equals("exception")) {
            throw new HelloException("hello");
        }
        return "hello, " + name;
    }

    @Override
    public void async(String name) {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
