package com.dong.rpc.client.factory;

import com.dong.rpc.client.RPCProxy;
import org.springframework.beans.factory.FactoryBean;

/**
 * @author caolidong
 * @date 17/8/21.
 */
public class ImportedServiceFactory<T> implements FactoryBean<T> {

    private RPCProxy proxy;

    private Class<?> clazz;

    public ImportedServiceFactory(Class<?> clazz,RPCProxy proxy) {
        this.proxy = proxy;
        this.clazz = clazz;
    }

    @Override
    public T getObject() throws Exception {
        return proxy.create(this.clazz);
    }

    @Override
    public Class<?> getObjectType() {
        return this.clazz;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
