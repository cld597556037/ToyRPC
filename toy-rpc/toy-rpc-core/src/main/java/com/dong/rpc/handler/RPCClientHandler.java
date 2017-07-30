package com.dong.rpc.handler;

import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.util.RPCMapHelper;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.BlockingQueue;

/**
 * @author caolidong
 * @date 17/6/25.
 */
public class RPCClientHandler  extends SimpleChannelInboundHandler<RPCResponse> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCResponse rpcResponse) throws Exception {
        BlockingQueue<RPCResponse> blockingQueue = RPCMapHelper.queueMap.get(rpcResponse.getRequestId());
        if (blockingQueue != null) {
            blockingQueue.put(rpcResponse);
        }
    }
}
