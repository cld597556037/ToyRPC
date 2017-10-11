package com.dong.rpc;

import com.dong.rpc.extension.SpiMeta;
import com.dong.rpc.rpc.Channel;
import com.dong.rpc.rpc.ChannelFactory;
import com.dong.rpc.rpc.MessageHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caolidong
 * @date 2017/9/30.
 */
@SpiMeta(name = "default")
public class NettyChannelFactory implements ChannelFactory {

    protected Map<String, Channel> port2ServerChannel = new HashMap<String, Channel>();

    @Override
    public Channel createServer(int port, MessageHandler messageHandler) {
        String key = String.valueOf(port);
        Channel channel = port2ServerChannel.get(key);
        if (channel == null) {
            channel = new NettyServer(port, messageHandler);
            port2ServerChannel.put(key, channel);
            channel.open();
        }
        return channel;
    }

    @Override
    public Channel createClient(String address) {
        return new NettyClient(address);
    }
}
