package com.netty.constant;

/**
 * @author min
 */
public class Command {
    /**
     * 心跳包
     */
    public static final Byte HEART_BEAT = 0;
    /**
     * 登录请求
     */
    public static final Byte LOGIN_REQUEST = 1;
    /**
     * 登录响应
     */
    public static final Byte LOGIN_RESPONSE = 2;
    /**
     * 消息请求
     */
    public static final Byte MESSAGE_REQUEST = 3;
    /**
     * 消息响应
     */
    public static final Byte MESSAGE_RESPONSE = 4;
    /**
     * 创建群聊
     */
    public static final Byte CREATE_GROUP_REQUEST = 5;
    /**
     * 创建群聊响应
     */
    public static final Byte CREATE_GROUP_RESPONSE = 6;
    /**
     * 发送群聊
     */
    public static final Byte GROUP_MESSAGE_REQUEST = 7;
    /**
     * 创建群聊响应
     */
    public static final Byte GROUP_MESSAGE_RESPONSE = 8;
    /**
     * 默认错误
     */
    public static final Byte DEFAULT_ERROR = 127;
}
