package com.dong.rpc.service.hello;


import com.dong.rpc.client.annotation.Async;
import com.dong.rpc.exception.HelloException;
import com.dong.rpc.listener.HelloListener;

/**
 * @author caolidong
 * @date 17/6/29.
 */
public interface HelloService {

    String hello(String name) throws HelloException;

    @Async(HelloListener.class)
    void async(String name);
}
