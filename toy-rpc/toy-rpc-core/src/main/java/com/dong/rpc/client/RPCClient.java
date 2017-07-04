package com.dong.rpc.client;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.codec.RPCDecoder;
import com.dong.rpc.codec.RPCEncoder;
import com.dong.rpc.registry.ServiceDiscovery;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author caolidong
 * @date 17/6/26.
 */
public class RPCClient {

    private static Logger logger = Logger.getLogger(RPCClient.class);

    private AtomicLong atomicLong = new AtomicLong();

    private Channel channel;

    private ServiceDiscovery serviceDiscovery;

    public RPCClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public RPCResponse sendMessage(Class<?> clazz, Method method, Object[] args) {
        RPCResponse rpcResponse = null;
        RPCRequest rpcRequest = getRequest(clazz, method, args);
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup(1))
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new RPCDecoder(10 * 1024 * 1024, 1,4, 0, 0))
                                .addLast(new RPCEncoder())
                                .addLast(new RPCClientHandler())
                        ;
                    }
                });
        try {
            BlockingQueue<RPCResponse> blockingQueue = new ArrayBlockingQueue<>(1);
            RPCMapHelper.queueMap.put(rpcRequest.getRequestId(), blockingQueue);
            String serviceAddress = serviceDiscovery.disover(clazz.getName()).get(0);
            String[] splits = serviceAddress.split(":");
            String host = splits[0];
            int port = Integer.parseInt(splits[1]);
            logger.info(String.format("向%s发出rpc调用：%s", serviceAddress, rpcRequest));
            ChannelFuture channelFuture = bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .connect(host, port).sync();
            channelFuture.channel().writeAndFlush(rpcRequest);
            rpcResponse = blockingQueue.poll(10 * 1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            RPCMapHelper.queueMap.remove(rpcRequest.getRequestId());
        }
        logger.info("RPC调用返回：" + rpcResponse);
        return  rpcResponse;
    }

    private RPCRequest getRequest(Class<?> clazz, Method method, Object[] args) {
        RPCRequest rpcRequest = new RPCRequest();
        rpcRequest.setRequestId(atomicLong.incrementAndGet());
        rpcRequest.setClazz(clazz);
        rpcRequest.setMethod(method.getName());
        rpcRequest.setParams(args);
        rpcRequest.setParamTypes(method.getParameterTypes());
        return rpcRequest;
    }
}
