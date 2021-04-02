package com.netty.packet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Header {
    //魔数（4字节） + 版本号（1字节） + 序列化算法（1字节） + 指令（1字节） + 数据长度（4字节)
    /**
     * 魔数
     */
    @Builder.Default
    private int magic = 0x88888888;
    /**
     * 版本号
     */
    @Builder.Default
    private byte version = 1;
    /**
     * 加密解密方式
     */
    @Builder.Default
    private byte crypt = 1;
    /**
     * 消息类型
     */
    private byte cmd;
    /**
     * 消息体长度
     */
    private int dataLen;
}
