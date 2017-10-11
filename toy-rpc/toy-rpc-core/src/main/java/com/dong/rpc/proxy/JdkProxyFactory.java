package com.dong.rpc.proxy;

import com.dong.rpc.extension.SpiMeta;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/**
 * jdkProxy工厂类
 *
 * @author caolidong
 * @date 2017/10/11
 */
@SpiMeta
public class JdkProxyFactory implements ProxyFactory {
    @Override
    public <T> T createProxy(Class<T> clazz, InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, invocationHandler);
    }
}
