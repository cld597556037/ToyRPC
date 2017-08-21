package com.dong.rpc.client;

import com.dong.rpc.channel.ChannelWrapper;
import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.registry.ServiceDiscovery;
import com.dong.rpc.trace.RPCHolder;
import com.dong.rpc.trace.RPCTrace;
import com.dong.rpc.util.RPCMapHelper;
import io.netty.channel.Channel;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author caolidong
 * @date 17/6/26.
 */
public class RPCClient {

    private static Logger logger = Logger.getLogger(RPCClient.class);

    private static int nThreads = Runtime.getRuntime().availableProcessors() * 2;

    private static ExecutorService handlerPool = Executors.newFixedThreadPool(nThreads);

    private AtomicLong atomicLong = new AtomicLong();

    public static ConcurrentHashMap<String, ChannelWrapper> channelWrappers = new ConcurrentHashMap<>();

    private ServiceDiscovery serviceDiscovery;

    public RPCClient(ServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    public RPCResponse sendMessage(Class<?> clazz, Method method, Object[] args) throws Exception {
        if (!RPCHolder.hasTrace()) {
            //第一层调用初始化跟踪信息
            RPCHolder.init();
        } else {
            RPCTrace trace = RPCHolder.getTrace();
            trace.setRequestTime(Calendar.getInstance().getTime());
            trace.setSeq(trace.getSeq() + 1);
        }
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
            rpcResponse = blockingQueue.poll(5 * 1000, TimeUnit.MILLISECONDS);
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
        RPCHolder.getTrace().setResponseTime(Calendar.getInstance().getTime());
        if (RPCHolder.getTrace().getSeq() == 1) {
            //链式调用请求发起者 清楚本次跟踪信息
            RPCHolder.remove();
        }
        if (rpcResponse == null) {
            throw new TimeoutException();
        }
        logger.info("RPC调用返回：" + rpcResponse);
        return  rpcResponse;
    }

    //异步处理
    public void sendMessageAyns(Class<?> clazz, Method method, Object[] args, Class<?> listenerClass) throws Exception {
        handlerPool.submit(() -> {
                RPCResponse rpcResponse = null;
                Listener listener = null;
                if (listenerClass != null && Listener.class.isAssignableFrom(listenerClass)) {
                    try {
                        listener = (Listener)listenerClass.newInstance();
                    } catch (InstantiationException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    rpcResponse = sendMessage(clazz, method, args);
                } catch (TimeoutException te) {
                    if (listener != null) {
                        logger.info("async send message timeout");
                        listener.onTimeout();
                    }
                } catch (Exception e) {
                    logger.info("async send message error:" + e);
                }
                if (rpcResponse.hasThrowable()) {
                    logger.info("async send message exception:" + rpcResponse.getRequestId());
                    listener.onException(rpcResponse.getThrowable());
                } else {
                    logger.info("async send message succeed:" + rpcResponse.getRequestId());
                    listener.onComplete();
                }
            });

    }

    private RPCRequest getRequest(Class<?> clazz, Method method, Object[] args) {
        RPCRequest rpcRequest = new RPCRequest();
        rpcRequest.setRequestId(atomicLong.incrementAndGet());
        rpcRequest.setClazz(clazz);
        rpcRequest.setMethod(method.getName());
        rpcRequest.setParams(args);
        rpcRequest.setParamTypes(method.getParameterTypes());
        rpcRequest.setRequestTime(Calendar.getInstance().getTime().getTime());
        rpcRequest.setTrace(RPCHolder.getTrace());
        return rpcRequest;
    }

    /**
     * 1.关闭发现服务
     * 2.channel池对象
     * 3.netty关闭
     */
    public void close() {}

}
