package com.dong.rpc.rpc.impl;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;
import com.dong.rpc.rpc.Cluster;
import com.dong.rpc.rpc.HaStrategy;
import com.dong.rpc.rpc.Invoker;
import com.dong.rpc.rpc.LoadBalance;
import com.dong.rpc.rpc.Protocol;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author caolidong
 * @date 2017/9/29.
 */
public class DefaultCluster<T> implements Cluster<T> {

    private Class<T> interfaceClass;

    private Protocol protocol;

    private List<Invoker<T>> invokers = new ArrayList<>();

    private HaStrategy haStrategy;

    private LoadBalance<T> loadBalance;

    public DefaultCluster(Class<T> interfaceClass, Protocol protocol) {
        this.interfaceClass = interfaceClass;
        this.protocol = protocol;
    }

    @Override
    public RPCResponse invoke(RPCRequest request) throws ToyException {
        return haStrategy.invoke(request, loadBalance);
    }

    /**
     * 更新invokers
     *
     * @param addresses
     */
    @Override
    public void notify(List<String> addresses) {
        //移除失效Invoker
        invokers = invokers.stream().filter(invoker ->
                addresses.contains(invoker.getAddress())).collect(Collectors.toList());
        //添加新增address
        invokers = addresses.stream().map(address -> getInvoker(address)).collect(Collectors.toList());
    }

    /**
     * @param address
     * @return
     */
    private Invoker<T> getInvoker(String address) {
        for (Invoker<T> invoker : invokers) {
            if (address.equals(invoker.getAddress())) {
                return invoker;
            }
        }
        return protocol.refer(interfaceClass, address);
    }

    @Override
    public List<Invoker<T>> getInvokers() {
        return invokers;
    }

    @Override
    public void setHaStrategy(HaStrategy haStrategy) {
        this.haStrategy = haStrategy;
    }

    @Override
    public void setLoadBalance(LoadBalance<T> loadBalance) {
        this.loadBalance = loadBalance;
    }
}
