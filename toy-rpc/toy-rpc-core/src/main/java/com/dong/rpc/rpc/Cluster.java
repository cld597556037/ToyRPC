package com.dong.rpc.rpc;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;

import java.util.List;

/**
 * @author caolidong
 * @date 2017/9/29.
 */
public interface Cluster<T> {

    RPCResponse invoke(RPCRequest request) throws ToyException;

    void notify(List<String> addresses);
}
