package com.dong.rpc.entity;

import com.dong.rpc.trace.RPCTrace;

import java.util.Arrays;

/**
 * RPC请求
 * @author caolidong
 * @date 17/6/25.
 */
public class RPCRequest {

    /**
     * 请求id
     */
    private Long requestId;

    /**
     * 请求时间
     */
    private Long requestTime;

    /**
     *  调用类名
     */
    private Class<?> clazz;

    /**
     * 调用方法名
     */
    private String method;

    /**
     * 方法参数类型
     */
    private Class<?>[] paramTypes;

    /**
     * 方法参数
     */
    private Object[] params;

    /**
     * trace信息
     * @return
     */
    private RPCTrace trace;

    public Long getRequestId() {
        return requestId;
    }

    public void setRequestId(Long requestId) {
        this.requestId = requestId;
    }

    public Long getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Long requestTime) {
        this.requestTime = requestTime;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Class<?>[] getParamTypes() {
        return paramTypes;
    }

    public void setParamTypes(Class<?>[] paramTypes) {
        this.paramTypes = paramTypes;
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public RPCTrace getTrace() {
        return trace;
    }

    public void setTrace(RPCTrace trace) {
        this.trace = trace;
    }

    @Override
    public String toString() {
        return "RPCRequest [" +
                "requestId=" + requestId +
                ", requestTime=" + requestTime +
                ", clazz=" + clazz +
                ", method='" + method + '\'' +
                ", paramTypes=" + Arrays.toString(paramTypes) +
                ", params=" + Arrays.toString(params) +
                ", trace=" + trace +
                "]";
    }
}
