package com.netty;

import com.netty.server.TCPServer;

/**
 * @author min
 */
public class SocketServer {
    public static void main(String[] args) {
        TCPServer.getSingletonInstance().run();
        System.out.println("+++++++++++++++++++++++++++++");
    }
}
