package com.netty.handler;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class LengthFieldFrameDecoder extends LengthFieldBasedFrameDecoder {

    /**
     * - maxFrameLength：解码的帧的最大长度
     * - lengthFieldOffset：长度属性的起始位（偏移位），包中存放有整个大数据包长度的字节，这段字节的其实位置
     * - lengthFieldLength：长度属性的长度，即存放整个大数据包长度的字节所占的长度
     * - lengthAdjustmen：长度调节值，在总长被定义为包含包头长度时，修正信息长度。
     * - initialBytesToStrip：跳过的字节数，根据需要我们跳过lengthFieldLength个字节，以便接收端直接接受到不含“长度属性”的内容
     * - failFast ：为true，当frame长度超过maxFrameLength时立即报TooLongFrameException异常，为false，读取完整个帧再报异常
     */

    /**
     * 解码的帧的最大长度（字节） 必须 >= lengthFieldOffset + lengthFieldLength
     */
    private static final int maxFrameLength = 1024;
    /**
     * 消息长度字段在开始的第几个字节
     */
    private static final int lengthFieldOffset = 7;
    /**
     * 消息长度字段的占几个字节
     */
    private static final int lengthFieldLength = 4;

    public LengthFieldFrameDecoder() {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
    }
}
