package com.dong.rpc.proxy;

import com.dong.rpc.extension.Spi;

import java.lang.reflect.InvocationHandler;

/**
 * @author caolidong
 * @date 2017/10/11.
 */
@Spi
public interface ProxyFactory {

    public <T> T createProxy(Class<T> clazz, InvocationHandler invocationHandler);
}
