package com.dong.rpc.rpc.impl.loadBalance;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.extension.SpiMeta;
import com.dong.rpc.rpc.Cluster;
import com.dong.rpc.rpc.Invoker;
import com.dong.rpc.rpc.LoadBalance;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author caolidong
 * @date 2017/10/12.
 */
@SpiMeta(name = "random")
public class RandomLoadBalance<T> implements LoadBalance<T> {

    private Cluster<T> cluster;

    public Cluster<T> getCluster() {
        return cluster;
    }

    @Override
    public void setCluster(Cluster<T> cluster) {
        this.cluster = cluster;
    }

    @Override
    public Invoker<T> select(RPCRequest request) {
        List<Invoker<T>> invokers = this.cluster.getInvokers();
        if (invokers.size() == 0) {
            throw new RuntimeException("no invoker available");
        }
        return invokers.get(ThreadLocalRandom.current().nextInt(0, invokers.size()));
    }

}
