package com.dong.rpc.rpc;

/**
 * @author caolidong
 * @date 2017/10/13.
 */
public interface Closeable {

    /**
     * 开启节点
     */
    void open();

    /**
     * 关闭节点
     */
    void close();

}
