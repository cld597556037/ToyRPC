package com.dong.rpc.registry.impl;

import com.dong.rpc.channel.ChannelWrapper;
import com.dong.rpc.registry.ServiceDiscovery;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * @author caolidong
 * @date 17/6/29.
 */
public class LocalServiceDiscovery implements ServiceDiscovery {

    private static final List<String> addresses = Arrays.asList("localhost:8765", "localhost:8766");

    private static List<ChannelWrapper> channelWrappers;

    static {
        channelWrappers = addresses.stream().map(address-> {
            String[] splits = address.split(":");
            return new ChannelWrapper(splits[0], Integer.parseInt(splits[1]));
        }).collect(Collectors.toList());
    }

    @Override
    public List<String> discover(String serviceName) {
        return addresses;
    }

    @Override
    public ChannelWrapper getChannelWrapper(String serviceName) {
        return channelWrappers.get(ThreadLocalRandom.current().nextInt(addresses.size()));
    }

    @Override
    public void close() {

    }
}
