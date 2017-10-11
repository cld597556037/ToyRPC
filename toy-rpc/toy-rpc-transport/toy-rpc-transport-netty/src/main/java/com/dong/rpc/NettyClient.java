package com.dong.rpc;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;
import com.dong.rpc.rpc.Channel;
import com.dong.rpc.util.RPCMapHelper;
import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * @author caolidong
 * @date 2017/9/29.
 */
public class NettyClient implements Channel {

    private static Logger logger = Logger.getLogger(NettyClient.class);

    private ChannelWrapper channelWrapper;

    private String ip;
    private int port;

    private volatile boolean init;

    public NettyClient(String address) {
        String[] splits = address.split(":");
        this.ip = splits[0];
        this.port = Integer.parseInt(splits[1]);
    }

    @Override
    public RPCResponse request(RPCRequest request) throws ToyException {
        io.netty.channel.Channel channel = null;
        RPCResponse response = null;
        try {
            channel = channelWrapper.getChannel();
            //todo 使用future实现
            BlockingQueue<RPCResponse> blockingQueue = new ArrayBlockingQueue<>(1);
            RPCMapHelper.queueMap.put(request.getRequestId(), blockingQueue);
            channel.writeAndFlush(request);
            response = blockingQueue.poll(5 * 1000, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (channel != null) {
                try {
                    channelWrapper.returnChannel(channel);
                } catch (Exception e) {
                    return response;
                }
            }
            return response;
        }
    }

    @Override
    public synchronized void open() {
        if (init) {
            return;
        }
        channelWrapper = new ChannelWrapper(ip, port, NettyClient.this, (channel, message) -> {
            if (message instanceof RPCResponse) {
                RPCResponse rpcResponse = (RPCResponse) message;
                BlockingQueue<RPCResponse> blockingQueue = RPCMapHelper.queueMap.get(rpcResponse.getRequestId());
                if (blockingQueue != null) {
                    try {
                        blockingQueue.put(rpcResponse);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        });
        init = true;
    }

    @Override
    public void close() {
        logger.info(String.format("close netty client on: %s", ip + ":" + port));
        channelWrapper.close();
    }
}
