package com.dong.rpc.handler;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.trace.RPCHolder;
import com.dong.rpc.trace.RPCTrace;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author caolidong
 * @date 17/6/25.
 */
public class RPCServerHandler extends SimpleChannelInboundHandler<RPCRequest> {

    private static Logger logger = Logger.getLogger(RPCServerHandler.class);

    /**
     * 提供服务的实际对象map
     */
    private Map<String, Object> services;


    public RPCServerHandler(Map<String, Object> services) {
        this.services = services;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RPCRequest rpcRequest) throws Exception {
        logger.info("server recieve request: " + rpcRequest);

        //当前的seq为父级调用 id<<1
        RPCTrace trace = rpcRequest.getTrace();
        trace.setParentId(trace.getSeq());
        trace.setSeq(trace.getSeq() * 10);
        RPCHolder.setTrace(trace);

        long requestId = rpcRequest.getRequestId();

        String methodName = rpcRequest.getMethod();
        Object[] params = rpcRequest.getParams();
        Class<?>[] paramTypes = rpcRequest.getParamTypes();

        Object target = services.get(rpcRequest.getClazz().getName());

        Method method = target.getClass().getDeclaredMethod(methodName, paramTypes);
        RPCResponse  rpcResponse = new RPCResponse();
        rpcResponse.setRequestId(requestId);
        try {
            rpcResponse.setResponse(method.invoke(target, params));
        } catch (InvocationTargetException e) {
            rpcResponse.setThrowable(e.getTargetException());
        }
        logger.info("server send response: " + rpcResponse);
        channelHandlerContext.channel().writeAndFlush(rpcResponse);
        RPCHolder.remove();
    }

    public void channelActive(ChannelHandlerContext ctx) {
        logger.info("channel active from: " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exception caught" + cause);
        ctx.channel().close();
    }
}
