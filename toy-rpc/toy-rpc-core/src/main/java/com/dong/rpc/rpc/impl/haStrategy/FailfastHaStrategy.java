package com.dong.rpc.rpc.impl.haStrategy;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.SpiMeta;
import com.dong.rpc.rpc.HaStrategy;
import com.dong.rpc.rpc.LoadBalance;

/**
 * @author caolidong
 * @date 2017/10/12.
 */
@SpiMeta(name = "failfast")
public class FailfastHaStrategy implements HaStrategy {

    @Override
    public RPCResponse invoke(RPCRequest request, LoadBalance<?> loadBalance) throws ToyException {
        return loadBalance.select(request).invoke(request);
    }

}
