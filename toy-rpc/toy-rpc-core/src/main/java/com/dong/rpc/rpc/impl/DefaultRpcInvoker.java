package com.dong.rpc.rpc.impl;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.ExtensionLoader;
import com.dong.rpc.rpc.Channel;
import com.dong.rpc.rpc.ChannelFactory;
import com.dong.rpc.rpc.Invoker;
import org.apache.log4j.Logger;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author caolidong
 * @date 2017/9/29.
 */
public class DefaultRpcInvoker<T> implements Invoker<T> {

    private static Logger logger = Logger.getLogger(DefaultRpcInvoker.class);

    private Class<T> clz;
    private String address;
    private AtomicLong count = new AtomicLong(0);
    private Channel channel;
    private ChannelFactory channelFactory;
    private boolean available = false;

    public DefaultRpcInvoker(Class<T> clz, String address) {
        this.clz = clz;
        this.address = address;
        init();
    }

    private void init() {
        try {
            channelFactory = ExtensionLoader.getExtensionLoader(ChannelFactory.class).getExtension();
            channel = channelFactory.createClient(address);
            channel.open();
        } catch (ToyException e) {
            logger.error("init channel error: " + address, e);
        }
        this.available = true;
    }

    @Override
    public RPCResponse invoke(RPCRequest request) throws ToyException {
        count.addAndGet(1l);
        return channel.request(request);
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String getAddress() {
        return address;
    }
}
