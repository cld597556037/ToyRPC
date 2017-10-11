package com.dong.rpc.config.api;

import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.ExtensionLoader;
import com.dong.rpc.proxy.ProxyFactory;
import com.dong.rpc.proxy.RequestInvocationHandler;
import com.dong.rpc.rpc.Cluster;
import com.dong.rpc.rpc.impl.DefaultCluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;

/**
 * 服务消费者Config
 * @author caolidong
 * @date 17/8/30.
 */
public class ConsumerConfig<T> {

    private Logger logger = LoggerFactory.getLogger(ConsumerConfig.class);

    private String interfaceClass;

    private ProtocolConfig protocolConfig;

    private DiscoveryConfig discoveryConfig;

    private T refer;

    public String getInterface() {
        return interfaceClass;
    }

    public void setInterface(String interfaceClass) {
        this.interfaceClass = interfaceClass;
    }

    public void setProtocolConfig(ProtocolConfig protocolConfig) {
        this.protocolConfig = protocolConfig;
    }

    public void setDiscoveryConfig(DiscoveryConfig discoveryConfig) {
        this.discoveryConfig = discoveryConfig;
    }

    public ProtocolConfig getProtocolConfig() {
        return protocolConfig;
    }

    public DiscoveryConfig getDiscoveryConfig() {
        return discoveryConfig;
    }

    public T refer() {
        if (refer == null) {
            init();
        }
        return refer;
    }

    private void init() {
        logger.info("refer consumer:" + this);
        Class<T> clazz = null;
        Cluster<T> cluster = null;
        InvocationHandler invocationHandler = null;
        ProxyFactory proxyFactory = null;
        try {
            clazz = (Class) Class.forName(interfaceClass);
            cluster= new DefaultCluster(clazz, this.protocolConfig.getProtocol());
            discoveryConfig.getServiceDisovery().subscribe(interfaceClass, cluster);
            invocationHandler = new RequestInvocationHandler(clazz, cluster);
            proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension();
            refer = proxyFactory.createProxy(clazz, invocationHandler);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (ToyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "ConsumerConfig [" +
                "interfaceClass='" + interfaceClass + '\'' +
                "]";
    }
}
