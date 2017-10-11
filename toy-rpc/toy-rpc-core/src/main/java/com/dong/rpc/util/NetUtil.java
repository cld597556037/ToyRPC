package com.dong.rpc.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author caolidong
 * @date 17/6/25.
 */
public class NetUtil {

    /**
     * 获取本地ip
     *
     * @return
     */
    public static String getLocalIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return null;
    }
}
