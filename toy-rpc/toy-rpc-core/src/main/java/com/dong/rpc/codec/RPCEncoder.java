package com.dong.rpc.codec;

import com.dong.rpc.serialize.KryoSerializer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author caolidong
 * @date 17/6/25.
 */
public class RPCEncoder extends MessageToByteEncoder<Object> {

    private static byte magic = 0x01;

    private KryoSerializer serializer = new KryoSerializer();

    @Override
    protected void encode(ChannelHandlerContext channel, Object obj, ByteBuf out) throws Exception {
        byte[] bytes = serializer.serialize(obj);
        out.writeByte(magic);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
