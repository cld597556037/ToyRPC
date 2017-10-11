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
@SpiMeta
public class NettyChannelFactory implements ChannelFactory {

    protected Map<String, Channel> port2ServerChannel = new HashMap<String, Channel>();

    protected Map<String, Channel> port2ClientChannel = new HashMap<String, Channel>();

    @Override
    public Channel createServer(int port, MessageHandler messageHandler) {
        String key = String.valueOf(port);
        port2ServerChannel.putIfAbsent(key, new NettyServer(port, messageHandler));
        return port2ServerChannel.get(key);
    }

    @Override
    public Channel createClient(String address) {
        port2ClientChannel.putIfAbsent(address, new NettyClient(address));
        return port2ClientChannel.get(address);
    }

    @Override
    public void release() {
        port2ClientChannel.entrySet().stream().forEach(entry -> entry.getValue().close());
        port2ServerChannel.entrySet().stream().forEach(entry -> entry.getValue().close());
    }
}
