package com.dong.rpc.server;

import com.dong.rpc.codec.RPCDecoder;
import com.dong.rpc.codec.RPCEncoder;
import com.dong.rpc.handler.RPCServerHandler;
import com.dong.rpc.registry.ServiceRegistry;
import com.dong.rpc.util.NetUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.internal.StringUtil;
import org.apache.log4j.Logger;

import java.util.Map;


/**
 * @author caolidong
 * @date 17/6/25.
 */
public class RPCServerImpl implements RPCServer {

    private static Logger logger = Logger.getLogger(RPCServerImpl.class);

    private String localIp;

    private int port;

    private ServiceRegistry serviceRegistry;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    private Map<String, Object> services;

    public RPCServerImpl(int port, ServiceRegistry serviceRegistry, RPCServiceInitializer initializer) {
        this.port = port;
        this.serviceRegistry = serviceRegistry;
        this.services = initializer.getServiceMap();
    }

    public RPCServerImpl(int port, ServiceRegistry serviceRegistry, Map<String, Object> services) {
        this.port = port;
        this.serviceRegistry = serviceRegistry;
        this.services = services;
    }

    @Override
    public void start() {
        logger.info("RPC Server start at " + getLocalIp() + ":" + port);
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast("decoder",
                                new RPCDecoder(10 * 1024 * 1024, 1 , 4, 0,0  ))
                            .addLast("encoder", new RPCEncoder())
                            .addLast(new RPCServerHandler(services));
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();
            services.entrySet().stream().forEach(entry -> {
                serviceRegistry.register(entry.getKey(), getLocalIp() + ":" + port);
            });

            future.channel().closeFuture().sync();
        } catch (Exception e) {
            logger.error("server 异常", e);

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }

    }

    @Override
    public void shutdown() {

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
