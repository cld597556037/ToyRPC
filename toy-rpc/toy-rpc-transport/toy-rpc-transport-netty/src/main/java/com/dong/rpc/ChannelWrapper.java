package com.dong.rpc;

import com.dong.rpc.rpc.MessageHandler;
import io.netty.channel.Channel;
import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * Channel对象池封装类
 *
 * @author caolidong
 * @date 17/7/18.
 */
public class ChannelWrapper {

    private String host;

    private int port;

    private com.dong.rpc.rpc.Channel channel;

    private ObjectPool<Channel> channelObjectPool;

    private MessageHandler messageHandler;

    public ChannelWrapper(String host, int port, com.dong.rpc.rpc.Channel channel, MessageHandler messageHandler) {
        this.host = host;
        this.port = port;
        this.channel = channel;
        this.messageHandler = messageHandler;
        channelObjectPool = new GenericObjectPool<Channel>(new ChannelPooledObjectFactory(host, port, channel, messageHandler));
    }

    @Override
    public String toString() {
        return "ChannelWrapper [" +
                "host='" + host + '\'' +
                ", port=" + port +
                ", channelObjectPool=" + channelObjectPool +
                "]";
    }

    public Channel getChannel() throws Exception {
        return channelObjectPool.borrowObject();
    }

    public void returnChannel(Channel channel) throws Exception {
        channelObjectPool.returnObject(channel);
    }

    public void close() {
        channelObjectPool.close();
    }
}
