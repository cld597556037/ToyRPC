package com.dong.rpc.entity;

/**
 * RPC响应
 * @author caolidong
 * @date 17/6/25.
 */
public class RPCResponse {

    /**
     *
     */
    private long requestId;

    /**
     *
     */
    private Object Response;

    /**
     *
     */
    private Throwable throwable;

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public Object getResponse() {
        return Response;
    }

    public void setResponse(Object response) {
        Response = response;
    }

    public Throwable getThrowable() {
        return throwable;
    }

    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    public boolean hasThrowable() {
        return throwable != null;
    }

    @Override
    public String toString() {
        return "RPCResponse [" +
                "requestId=" + requestId +
                ", Response=" + Response +
                ", throwable=" + throwable +
                "]";
    }
}
