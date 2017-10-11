package com.dong.rpc.rpc.impl;

import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.ExtensionLoader;
import com.dong.rpc.rpc.Channel;
import com.dong.rpc.rpc.ChannelFactory;
import com.dong.rpc.rpc.Exporter;
import com.dong.rpc.rpc.MessageHandler;
import com.dong.rpc.rpc.Provider;

/**
 * @author caolidong
 * @date 2017/10/9.
 */
public class DefaultRpcExporter<T> implements Exporter<T> {

    private Provider<T> provider;

    private Channel channel;

    private MessageHandler messageHandler;

    public DefaultRpcExporter(Provider<T> provider, MessageHandler messageHandler, int port) {
        this.provider = provider;
        this.messageHandler = messageHandler;
        try {
            ChannelFactory channelFactory = ExtensionLoader.getExtensionLoader(ChannelFactory.class).getExtension();
            channel = channelFactory.createServer(port, messageHandler);
            channel.open();
        } catch (ToyException e) {
            e.printStackTrace();
        }
    }

}
