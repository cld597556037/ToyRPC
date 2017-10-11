package com.dong.rpc.rpc.impl;

import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.ExtensionLoader;
import com.dong.rpc.extension.SpiMeta;
import com.dong.rpc.rpc.ChannelFactory;
import com.dong.rpc.rpc.Exporter;
import com.dong.rpc.rpc.Invoker;
import com.dong.rpc.rpc.Protocol;
import com.dong.rpc.rpc.Provider;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author caolidong
 * @date 2017/10/9.
 */
@SpiMeta
public class DefaultRpcProtocol implements Protocol {

    private static Logger logger = Logger.getLogger(DefaultRpcProtocol.class);

    /**
     * <port, messageHandler> 每个messageHandler只保存对应port上export的服务
     */
    private Map<String, ProviderRouterMessagHandler> messageHandlers = new HashMap<>();

    @Override
    public <T> Exporter<T> export(Class<T> clz, T impl, int port) {
        String key = String.valueOf(port);
        ProviderRouterMessagHandler messagHandler = messageHandlers.get(key);
        if (messagHandler == null) {
            messagHandler = new ProviderRouterMessagHandler();
            messageHandlers.putIfAbsent(key, messagHandler);
        }
        Provider provider = new DefaultProvider(clz, impl);
        Exporter<T> exporter = new DefaultRpcExporter<>(provider, messagHandler, port);
        messagHandler.addProvider(provider);
        logger.info(String.format("export exporter: %s at %s", clz.getSimpleName(), port));
        return exporter;
    }

    @Override
    public <T> Invoker<T> refer(Class<T> clz, String address) {
        Invoker<T> invoker = new DefaultRpcInvoker<>(clz, address);
        logger.info(String.format("refer invoker: %s at %s", clz.getSimpleName(), address));
        return invoker;
    }

    @Override
    public void destroy() {
        try {
            ChannelFactory channelFactory = ExtensionLoader.getExtensionLoader(ChannelFactory.class).getExtension();
            channelFactory.release();
        } catch (ToyException e) {

        }
    }
}
