package com.dong.rpc.rpc.impl;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.rpc.Provider;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author caolidong
 * @date 2017/10/9.
 */
public class DefaultProvider<T> implements Provider<T> {

    private Class<T> clazz;

    private T impl;

    public DefaultProvider(Class<T> clazz, T impl) {
        this.clazz = clazz;
        this.impl = impl;
    }

    @Override
    public Class<T> getInterface() {
        return this.clazz;
    }

    @Override
    public T getImpl() {
        return this.impl;
    }

    @Override
    public RPCResponse call(RPCRequest request) {
        String requestId = request.getRequestId();
        String methodName = request.getMethod();
        Object[] params = request.getParams();
        Class<?>[] paramTypes = request.getParamTypes();

        RPCResponse  rpcResponse = new RPCResponse();
        rpcResponse.setRequestId(requestId);
        try {
            Method method = impl.getClass().getDeclaredMethod(methodName, paramTypes);
            rpcResponse.setResponse(method.invoke(impl, params));
        } catch (InvocationTargetException ite) {
            rpcResponse.setThrowable(ite.getTargetException());
        } catch (Exception e) {
            rpcResponse.setThrowable(e);
        } finally {
            return rpcResponse;
        }
    }
}
