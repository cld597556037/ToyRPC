package com.dong.rpc.config.api;

import com.dong.rpc.exception.ToyException;
import com.dong.rpc.extension.ExtensionLoader;
import com.dong.rpc.rpc.Protocol;

/**
 * @author caolidong
 * @date 2017/10/11.
 */
public class ProtocolConfig {

    private Protocol protocol;

    public Protocol getProtocol() {
        if (protocol == null) {
            try {
                protocol = ExtensionLoader.getExtensionLoader(Protocol.class).getExtension();
            } catch (ToyException e) {
                e.printStackTrace();
            }
        }
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }
}
