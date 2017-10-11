package com.dong.rpc;

import com.dong.rpc.codec.NettyDecoder;
import com.dong.rpc.codec.NettyEncoder;
import com.dong.rpc.rpc.MessageHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.pool2.BasePooledObjectFactory;
import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.apache.log4j.Logger;

/**
 * Channel对象工厂
 *
 * @author caolidong
 * @date 17/7/18.
 */
public class ChannelPooledObjectFactory extends BasePooledObjectFactory<Channel> {

    private static Logger logger = Logger.getLogger(ChannelPooledObjectFactory.class);

    private String host;

    private int port;

    private com.dong.rpc.rpc.Channel channel;

    private MessageHandler messageHandler;

    public ChannelPooledObjectFactory(String host, int port, com.dong.rpc.rpc.Channel channel, MessageHandler messageHandler) {
        this.host = host;
        this.port = port;
        this.channel = channel;
        this.messageHandler = messageHandler;
    }

    @Override
    public Channel create() throws Exception {
        return createChannel();
    }

    @Override
    public PooledObject<Channel> wrap(Channel channel) {
        return new DefaultPooledObject<>(channel);
    }

    @Override
    public void destroyObject(PooledObject<Channel> p) throws Exception {
        //todo by caolidong  study
        Channel channel = p.getObject();
        channel.eventLoop().shutdownGracefully();
        channel.close().addListener(future -> logger.info("netty channel close finish"));
    }

    @Override
    public boolean validateObject(PooledObject<Channel> p) {
        return p.getObject().isActive();
    }

    private Channel createChannel() {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class)
                .group(new NioEventLoopGroup(1))
                .handler(new ChannelInitializer<Channel>() {
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new NettyDecoder(10 * 1024 * 1024, 1, 4, 0, 0))
                                .addLast(new NettyEncoder())
                                .addLast(new NettyChannelHandler(channel, messageHandler));
                    }
                });

        try {
            ChannelFuture future = bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .connect(host, port).sync();
            future.addListener(item -> {
                if (item.isSuccess()) {
                    logger.info("channel connect : " + host + " : " + port);
                }

            });
            Channel channel = future.channel();
            channel.closeFuture().addListener(item -> logger.info("channel close : " + host + " : " + port));
            return channel;
        } catch (InterruptedException e) {
            logger.error("channel error");
        }
        return null;
    }
}
