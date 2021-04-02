package com.netty;

import com.netty.client.TCPClient;

/**
 * @author min
 */
public class SocketClient {
    public static void main(String[] args) {
        System.out.println("SocketClient run");
        TCPClient.getSingletonInstance().login("limin", "123");
    }
}
