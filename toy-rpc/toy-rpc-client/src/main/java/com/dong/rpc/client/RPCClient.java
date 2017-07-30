package com.dong.rpc.client;

import com.dong.rpc.channel.ChannelWrapper;
import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.registry.ServiceDiscovery;
import com.dong.rpc.util.RPCMapHelper;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author caolidong
 * @date 17/6/26.
 */
public class RPCClient {

    private static Logger logger = Logger.getLogger(RPCClient.class);

    private AtomicLong atomicLong = new AtomicLong();

    public static ConcurrentHashMap<String, ChannelWrapper> channelWrappers = new ConcurrentHashMap<>();

    private ServiceDiscovery serviceDiscovery;

    public RPCClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public RPCResponse sendMessage(Class<?> clazz, Method method, Object[] args) throws Exception {
        RPCResponse rpcResponse = null;
        RPCRequest rpcRequest = getRequest(clazz, method, args);

        ChannelWrapper channelWrapper = serviceDiscovery.getChannelWrapper(clazz.getName());
        if (channelWrapper == null) {
            rpcResponse = new RPCResponse();
            RuntimeException runtimeException = new RuntimeException(String.format("no available service found of :%s",
                    clazz.getName()));
            rpcResponse.setRequestId(rpcRequest.getRequestId());
            rpcResponse.setThrowable(runtimeException);
            return rpcResponse;
        }

        Channel channel = null;
        try {

            channel = channelWrapper.getChannel();
            logger.info(String.format("%s 向%s发出rpc调用：%s", channel.localAddress(), channel.remoteAddress(), rpcRequest));
            if (channel == null) {
                rpcResponse = new RPCResponse();
                RuntimeException runtimeException = new RuntimeException("no channel available!");
                rpcResponse.setRequestId(rpcRequest.getRequestId());
                rpcResponse.setThrowable(runtimeException);
                return rpcResponse;
            }

            BlockingQueue<RPCResponse> blockingQueue = new ArrayBlockingQueue<>(1);
            RPCMapHelper.queueMap.put(rpcRequest.getRequestId(), blockingQueue);
            channel.writeAndFlush(rpcRequest);
            rpcResponse = blockingQueue.poll(10 * 1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channelWrapper.returnChannel(channel);
                } catch (Exception e) {
                    logger.info("return channel error" + channel.localAddress() + channel.isActive());
                    e.printStackTrace();
                }
            }

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
        rpcRequest.setRequestTime(Calendar.getInstance().getTime().getTime());
        return rpcRequest;
    }

    /**
     * 1.关闭发现服务
     * 2.channel池对象
     * 3.netty关闭
     */
    public void close() {}

}
