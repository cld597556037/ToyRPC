package com.dong.rpc.server;

import com.dong.rpc.codec.RPCDecoder;
import com.dong.rpc.codec.RPCEncoder;
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


/**
 * @author caolidong
 * @date 17/6/25.
 */
public class RPCServerImpl implements RPCServer {

    private static Logger logger = Logger.getLogger(RPCServerImpl.class);

    private String localIp;

    private int port;

    private boolean started = false;

    private Channel channel;

    private Object serviceImpl;

    private String serviceName;

    private ServiceRegistry serviceRegistry;

    private EventLoopGroup bossGroup = new NioEventLoopGroup();

    private EventLoopGroup workerGroup = new NioEventLoopGroup();

    public RPCServerImpl(int port, Object serviceImpl, String serviceName, ServiceRegistry serviceRegistry) {
        this.port = port;
        this.serviceImpl = serviceImpl;
        this.serviceName = serviceName;
        this.serviceRegistry = serviceRegistry;
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
                            .addLast(new RPCServerHandler(serviceImpl));
                    }
                });
        try {
            ChannelFuture future = serverBootstrap.bind(port).sync();

            serviceRegistry.register(serviceName, getLocalIp() + ":" + port);

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

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public Object getServiceImpl() {
        return serviceImpl;
    }

    public void setServiceImpl(Object serviceImpl) {
        this.serviceImpl = serviceImpl;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public EventLoopGroup getBossGroup() {
        return bossGroup;
    }

    public void setBossGroup(EventLoopGroup bossGroup) {
        this.bossGroup = bossGroup;
    }

    public EventLoopGroup getWorkerGroup() {
        return workerGroup;
    }

    public void setWorkerGroup(EventLoopGroup workerGroup) {
        this.workerGroup = workerGroup;
    }

}
