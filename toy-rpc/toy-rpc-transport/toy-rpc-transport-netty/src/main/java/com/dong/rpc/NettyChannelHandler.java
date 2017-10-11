package com.dong.rpc;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;
import com.dong.rpc.rpc.Channel;
import com.dong.rpc.rpc.MessageHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.apache.log4j.Logger;

/**
 * @author caolidong
 * @date 2017/9/30.
 */
public class NettyChannelHandler extends SimpleChannelInboundHandler {

    private Channel channel;

    private MessageHandler messageHandler;

    private static Logger logger = Logger.getLogger(NettyChannelHandler.class);

    public NettyChannelHandler(Channel channel, MessageHandler messageHandler) {
        this.channel = channel;
        this.messageHandler = messageHandler;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof RPCResponse) {
            logger.info("get response from: " + ctx.channel().remoteAddress());
            processResponse((RPCResponse)msg, ctx);
        } else if (msg instanceof RPCRequest) {
            logger.info("get request from: " + ctx.channel().remoteAddress());
            processRequest((RPCRequest)msg, ctx);
        }
    }

    private void processRequest(RPCRequest request, ChannelHandlerContext context) {
        RPCResponse response = null;
        try {
            response = (RPCResponse)messageHandler.handle(channel, request);
        } catch (ToyException e) {
            e.printStackTrace();
        }
        context.channel().writeAndFlush(response);
    }

    private void processResponse(RPCResponse response, ChannelHandlerContext context) {
        try {
            messageHandler.handle(channel, response);
        } catch (ToyException e) {
            e.printStackTrace();
        }
    }


    public void channelActive(ChannelHandlerContext ctx) {
        logger.debug("channel active from: " + ctx.channel().remoteAddress());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exception caught" + cause);
        ctx.channel().close();
    }
}
