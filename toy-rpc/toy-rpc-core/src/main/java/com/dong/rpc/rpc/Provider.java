package com.dong.rpc.rpc;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;

/**
 * @author caolidong
 * @date 2017/10/9.
 */
public interface Provider<T> {

    Class<T> getInterface();

    T getImpl();

    RPCResponse call(RPCRequest request);
}
