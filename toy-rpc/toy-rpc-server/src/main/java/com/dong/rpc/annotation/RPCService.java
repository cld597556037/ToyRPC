package com.dong.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * RPC服务注解
 * 将类自动加载到PRC服务列表中
 * @author caolidong
 * @date 17/7/24.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RPCService {
    /**
     * 服务接口类
     */
    Class<?> value() default Object.class ;
}
