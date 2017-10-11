package com.dong.rpc.rpc;

import com.dong.rpc.extension.Spi;

/**
 * @author caolidong
 * @date 2017/9/29.
 */
@Spi
public interface Protocol {

    <T> Exporter<T> export(Class<T> clz, T impl, int port);

    <T> Invoker<T> refer(Class<T> clz, String address);

    void destroy();
}
