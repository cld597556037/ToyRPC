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
    private Long traceId;

    /**
     * 父调用seqId
     */
    private Long parentId;

    /**
     * 当前调用序列
     */
    private Long seq;

    /**
     * 请求时间
     */
    private Date requestTime;

    /**
     * 响应时间
     */
    private Date responseTime;

    public Long getTraceId() {
        return traceId;
    }

    public void setTraceId(Long traceId) {
        this.traceId = traceId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(Long seq) {
        this.seq = seq;
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
                ", parentId=" + parentId +
                ", seq=" + seq +
                ", requestTime=" + requestTime +
                ", responseTime=" + responseTime +
                "]";
    }
}
