package com.dong.rpc.server;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author caolidong
 * @date 17/6/25.
 */
public class RPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {

    private static Logger logger = Logger.getLogger(RPCServerHandler.class);

    /**
     * 提供服务的实际对象
     */
    private Object service;

    public RPCServerHandler(Object service) {
        this.service = service;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest) throws Exception {
        logger.info("server recieve request: " + rpcRequest);
        long requestId = rpcRequest.getRequestId();

        String methodName = rpcRequest.getMethod();
        Object[] params = rpcRequest.getParams();
        Class<?>[] paramTypes = rpcRequest.getParamTypes();

        Method method = service.getClass().getDeclaredMethod(methodName, paramTypes);

        RPCResponse  rpcResponse = new RPCResponse();
        rpcResponse.setRequestId(requestId);
        try {
            rpcResponse.setResponse(method.invoke(service, params));
        } catch (InvocationTargetException e) {
            rpcResponse.setThrowable(e.getTargetException());
        }
        logger.info("server send response: " + rpcResponse);
        channelHandlerContext.channel().writeAndFlush(rpcResponse);
    }

    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("channel active from: " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exception caught" +cause);
        ctx.channel().close();
    }
}
