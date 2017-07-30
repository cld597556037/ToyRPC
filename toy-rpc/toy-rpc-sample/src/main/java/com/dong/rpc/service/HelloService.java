package com.dong.rpc.service;


import com.dong.rpc.exception.HelloException;

/**
 * @author caolidong
 * @date 17/6/29.
 */
public interface HelloService {

    String hello(String name) throws HelloException;
}
