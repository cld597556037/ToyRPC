package com.dong.rpc.rpc;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;

/**
 * @author caolidong
 * @date 2017/9/29.
 */
public interface Channel {

    RPCResponse request(RPCRequest request) throws ToyException;

    void open();

    void close();
}
