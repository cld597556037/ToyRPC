package com.dong.rpc.rpc;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.Spi;

/**
 * @author caolidong
 * @date 2017/10/12.
 */
@Spi
public interface HaStrategy {
    RPCResponse call(RPCRequest request, LoadBalance<?> loadBalance) throws ToyException;
}
