package com.dong.rpc.rpc;

import com.dong.rpc.extension.Spi;

/**
 * @author caolidong
 * @date 2017/9/30.
 */
@Spi
public interface ChannelFactory {
    /**
     * create server Channel
     * @param port
     * @param messageHandler
     * @return
     */
    Channel createServer(int port, MessageHandler messageHandler);

    /**
     * create client Channel
     * @param address
     * @return
     */
    Channel createClient(String address);

    /**
     * release channel resources
     */
    void release();
}
