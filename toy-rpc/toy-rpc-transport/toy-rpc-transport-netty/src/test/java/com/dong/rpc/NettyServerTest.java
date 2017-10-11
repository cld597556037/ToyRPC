package com.dong.rpc;

import com.dong.rpc.entity.RPCRequest;
import com.dong.rpc.entity.RPCResponse;
import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.ExtensionLoader;
import com.dong.rpc.rpc.Channel;
import com.dong.rpc.rpc.ChannelFactory;
import org.junit.Test;

/**
 * @author caolidong
 * @date 2017/9/30.
 */
public class NettyServerTest {

    @Test
    public void testNettyServer() {
        try {
            ChannelFactory factory = ExtensionLoader.getExtensionLoader(ChannelFactory.class).getExtension();
            Channel server = factory.createServer(9999, ((channel, message) -> {
                RPCRequest request = (RPCRequest) message;
                RPCResponse response = new RPCResponse();
                response.setRequestId(request.getRequestId());
                return response;
            }));
            server.open();
        } catch (ToyException e) {
            e.printStackTrace();
        }
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
