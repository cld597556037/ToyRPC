package com.dong.rpc.exception;

/**
 * @author caolidong
 * @date 2017/9/23.
 */
public class ToyException extends Exception {

    public ToyException(String message) {
        super(message);
    }

    public ToyException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
