package com.netty.packet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @author min
 */
@Data
@Slf4j
@Builder
@AllArgsConstructor
public class Body<T> {
    /**
     * 数据（N字节）
     */
    T message;
}
