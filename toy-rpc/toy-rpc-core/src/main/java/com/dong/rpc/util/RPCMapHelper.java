package com.dong.rpc.util;

import com.dong.rpc.entity.RPCResponse;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 网络通信请求响应辅助类
 * @author caolidong
 * @date 17/6/28.
 */
public class RPCMapHelper {

    /**
     * 方便根据requestId查询对应的rpcResponse
     */
    public static Map<Long, BlockingQueue<RPCResponse>> queueMap = new ConcurrentHashMap<>();
}
