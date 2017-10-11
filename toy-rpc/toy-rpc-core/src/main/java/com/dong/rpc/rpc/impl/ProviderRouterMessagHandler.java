package com.dong.rpc.rpc.impl;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.exception.ToyException;
import com.dong.rpc.rpc.Channel;
import com.dong.rpc.rpc.MessageHandler;
import com.dong.rpc.rpc.Provider;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caolidong
 * @date 2017/10/9.
 */
public class ProviderRouterMessagHandler implements MessageHandler {

    private Map<String, Provider<?>> providers = new HashMap<>();

    @Override
    public Object handle(Channel channel, Object message) throws ToyException {
        if (channel == null || message == null)  {
            throw new ToyException("ProviderRouterMessagHandler (channel, message) should not be null");
        }
        if (!(message instanceof RPCRequest)) {
            throw new ToyException("ProviderRouterMessagHandler unsurpported type:" + message.getClass());
        }

        RPCRequest request = (RPCRequest) message;
        String key = request.getClazz().getName();
        Provider provider = providers.get(key);
        if (provider == null) {
            throw new ToyException("Provider does not exist: " + key);
        }
        return provider.call(request);
    }

    public synchronized void addProvider(Provider<?> provider) {
        String key = provider.getInterface().getName();
        providers.putIfAbsent(key, provider);
    }
}
