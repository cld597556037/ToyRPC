package com.dong.rpc.rpc;

import com.dong.rpc.extension.Spi;

/**
 * @author caolidong
 * @date 2017/9/30.
 */
@Spi
public interface ChannelFactory {
    Channel createServer(int port, MessageHandler messageHandler);

    Channel createClient(String address);
}
