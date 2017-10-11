package com.dong.rpc.rpc.impl.loadBalance;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.extension.SpiMeta;
import com.dong.rpc.rpc.Invoker;
import com.dong.rpc.rpc.LoadBalance;

/**
 * @author caolidong
 * @date 2017/10/12.
 */
@SpiMeta(name = "failfast")
public class RandomLoadBalance<T> implements LoadBalance<T> {



    @Override
    public Invoker<T> select(RPCRequest request) {
        return null;
    }

}
