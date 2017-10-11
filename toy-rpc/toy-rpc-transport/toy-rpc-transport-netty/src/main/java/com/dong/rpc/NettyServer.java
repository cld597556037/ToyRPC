package com.dong.rpc;

import com.dong.rpc.codec.NettyDecoder;
import com.dong.rpc.codec.NettyEncoder;
import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;
import com.dong.rpc.rpc.Channel;
import com.dong.rpc.rpc.MessageHandler;
import com.dong.rpc.util.NetUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.StringUtil;
import org.apache.log4j.Logger;

/**
 * @author caolidong
 * @date 2017/9/30.
 */
public class NettyServer implements Channel {

    private static Logger logger = Logger.getLogger(NettyServer.class);

    private String localIp;

    private int port;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private MessageHandler messageHandler;

    private volatile boolean init;

    public NettyServer(int port, MessageHandler messageHandler) {
        this.port = port;
        this.messageHandler = messageHandler;
    }

    @Override
    public RPCResponse request(RPCRequest request) throws ToyException {
        throw new ToyException("request not supported at server side");
    }

    @Override
    public synchronized void open() {
        if (init) {
            return;
        }
        logger.info("RPC Server start at " + getLocalIp() + ":" + port);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("decoder",
                                new NettyDecoder(10 * 1024 * 1024, 1, 4, 0, 0))
                                .addLast("encoder", new NettyEncoder())
                                .addLast(new NettyChannelHandler(NettyServer.this, messageHandler));
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();
            //注册服务
            future.channel().closeFuture().addListener(closeFuture -> {
                    logger.info(String.format("close netty server on: %s", getLocalIp() + ":" + port));
            });
            init = true;
        } catch (Exception e) {
            logger.error("server启动 异常", e);
        }
    }

    @Override
    public void close() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }

    public String getLocalIp() {
        if (StringUtil.isNullOrEmpty(localIp)) {
            setLocalIp(NetUtil.getLocalIP());
        }
        return localIp;
    }

    public void setLocalIp(String localIp) {
        this.localIp = localIp;
    }
}
