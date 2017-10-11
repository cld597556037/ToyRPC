package com.dong.rpc.trace;

import java.io.Serializable;
import java.util.Date;

/**
 * RPC跟踪
 * @author caolidong
 * @date 17/8/21.
 */
public class RPCTrace implements Serializable {



    /**
     * 全局唯一跟踪id
     */
    private String traceId;

    /**
     * 父调用seqId
     */
    private String parentSpan;

    /**
     * 当前调用序列
     */
    private String span;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 响应时间
     */
    private Date responseTime;

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getParentSpan() {
        return parentSpan;
    }

    public void setParentSpan(String parentSpan) {
        this.parentSpan = parentSpan;
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(Date requestTime) {
        this.requestTime = requestTime;
    }

    public Date getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(Date responseTime) {
        this.responseTime = responseTime;
    }

    @Override
    public String toString() {
        return "RPCTrace [" +
                "traceId=" + traceId +
                ", parentSpan=" + parentSpan +
                ", span=" + span +
                ", requestTime=" + requestTime +
                ", responseTime=" + responseTime +
                "]";
    }
}
