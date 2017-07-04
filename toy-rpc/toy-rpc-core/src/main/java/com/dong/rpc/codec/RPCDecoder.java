package com.dong.rpc.codec;

import com.dong.rpc.serialize.KryoSerializer;
import com.dong.rpc.serialize.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

/**
 * @author caolidong
 * @date 17/6/25.
 */
public class RPCDecoder extends LengthFieldBasedFrameDecoder {

    private Serializer serializer = new KryoSerializer();

    public RPCDecoder(int maxFrameLength, int lengthFieldOffset, int lengthFieldLength, int lengthAdjustment, int initialBytesToStrip) {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength, lengthAdjustment, initialBytesToStrip);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in2) throws Exception {
        ByteBuf in = (ByteBuf) super.decode(ctx, in2);
        byte magic = in.readByte();
        int len = in.readInt();
        byte[] bytes = new byte[len];
        in.readBytes(bytes);
        return serializer.deserialize(bytes);
    }
}
