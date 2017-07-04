package com.dong.rpc.client;

import com.dong.rpc.entity.RPCResponse;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * RPC统一代理
 * @author caolidong
 * @date 17/6/26.
 */
public class RPCProxy {

    private static Method hashCodeMethod;
    private static Method equalsMethod;
    private static Method toStringMethod;
    static {
        try {
            hashCodeMethod = Object.class.getMethod("hashCode");
            equalsMethod = Object.class.getMethod("equals", Object.class);
            toStringMethod = Object.class.getMethod("toString");
        } catch (NoSuchMethodException e) {
            throw new NoSuchMethodError(e.getMessage());
        }
    }
    /**
     * 针对这几个方法做相应的策略
     * @param proxy
     * @return
     */
    private int proxyHashCode(Object proxy) {
        return System.identityHashCode(proxy);
    }
    private boolean proxyEquals(Object proxy, Object other) {
        return (proxy == other);
    }
    private String proxyToString(Object proxy) {
        return proxy.getClass().getName() + '@' + Integer.toHexString(proxy.hashCode());
    }

    private RPCClient rpcClient;

    public RPCProxy(RPCClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @SuppressWarnings("unchecked")
    public <T>T create(final Class<?> interfaceClass) {
        // 创建动态代理对象
        return (T) Proxy.newProxyInstance(
                interfaceClass.getClassLoader(),
                new Class<?>[]{interfaceClass},
                new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        if (hashCodeMethod.equals(method)) {
                            return proxyHashCode(proxy);
                        }
                        if (equalsMethod.equals(method)) {
                            return proxyEquals(proxy, args[0]);
                        }
                        if (toStringMethod.equals(method)) {
                            return proxyToString(proxy);
                        }
                        RPCResponse rpcResponse = rpcClient.sendMessage(interfaceClass, method, args);
                        if (rpcResponse.hasThrowable()){
                            throw rpcResponse.getThrowable();
                        }
                        return rpcResponse.getResponse();
                    }
                });

    }
}
