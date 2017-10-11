package com.dong.rpc;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.ExtensionLoader;
import com.dong.rpc.rpc.Channel;
import com.dong.rpc.rpc.ChannelFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author caolidong
 * @date 2017/9/30.
 */
public class NettyClientTest {

    @Test
    public void testNettyClient() {
        try {
            ChannelFactory factory = ExtensionLoader.getExtensionLoader(ChannelFactory.class).getExtension();
            Channel client = factory.createClient("127.0.0.1:9999");
            client.open();
            RPCRequest request = new RPCRequest();
            request.setRequestId("request");
            RPCResponse response = client.request(request);
            assertEquals(response.getRequestId(), request.getRequestId());
        } catch (ToyException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
