package com.dong.rpc.client.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 方法异步执行
 * @author caolidong
 * @date 17/8/1.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
//@Component
public @interface Async {

    Class<?> value() default Object.class;

}
