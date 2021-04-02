package com.netty.Serialzer;

import com.alibaba.fastjson.JSON;

import static com.netty.constant.SerializerAlgorithm.*;

/**
 * @author min
 */
public class JsonSerializer implements Serializer {

    private static volatile JsonSerializer instance;

    public static JsonSerializer getInstance() {
        if (instance == null) {
            synchronized (Serializer.class) {
                if (instance == null) {
                    instance = new JsonSerializer();
                }
            }
        }
        return instance;
    }

    @Override
    public byte getSerializerAlgorithm() {
        return JSON_SERIALIZE;
    }

    @Override
    public byte[] serialize(Object object) {
        return JSON.toJSONBytes(object);
    }

    @Override
    public <T> T deSerialize(Class<T> clazz, byte[] bytes) {
        return JSON.parseObject(bytes,clazz);
    }
}
