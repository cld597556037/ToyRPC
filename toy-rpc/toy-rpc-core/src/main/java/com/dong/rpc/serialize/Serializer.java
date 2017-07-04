package com.dong.rpc.serialize;

/**
 * Java对象序列化反序列化接口
 * @author caolidong
 * @date 17/6/25.
 */
public interface Serializer {

    /**
     * 将Java对象序列化为字节数组
     * @param obj
     * @return
     */
     byte[] serialize(Object obj);

    /**
     * 将字节数组反序列化为Java对象
     * @param bytes
     * @param <T>
     * @return
     */
     <T>T deserialize(byte[] bytes);

}
