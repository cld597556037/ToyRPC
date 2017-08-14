package com.dong.rpc.client;

/**
 * @author caolidong
 * @date 17/8/1.
 */
public interface Listener {

    void onComplete();

    void onException(Throwable throwable);

    void onTimeout();
}
