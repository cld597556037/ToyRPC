package com.dong.rpc.rpc;

import com.dong.rpc.exception.ToyException;

/**
 * @author caolidong
 * @date 2017/9/30.
 */
public interface MessageHandler {
    Object handle(Channel channel, Object message) throws ToyException;
}
