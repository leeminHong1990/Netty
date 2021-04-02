package com.netty.server;

import com.netty.handler.LengthFieldFrameDecoder;
import com.netty.handler.PackageCodecHandler;
import com.netty.server.handler.ServerHandler;
import com.netty.server.handler.ServerIdleHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author min
 */
public class TCPServer implements Runnable {

    private static final int port = 8888;
    private static volatile TCPServer instance = null;

    public static TCPServer getSingletonInstance() {
        if (instance == null) {
            synchronized (TCPServer.class) {
                if (instance == null) {
                    instance = new TCPServer();
                }
            }
        }
        return instance;
    }

    private TCPServer() {
    }

    /**
     * socket的标准参数
     *
     * ChannelOption.SO_BACKLOG, 1024
     * BACKLOG用于构造服务端套接字ServerSocket对象，标识当服务器请求处理线程全满时，用于临时存放已完成三次握手的请求的队列的最大长度。如果未设置或所设置的值小于1，Java将使用默认值50。
     *
     * ChannelOption.SO_KEEPALIVE, true
     * 是否启用心跳保活机制。在双方TCP套接字建立连接后（即都进入ESTABLISHED状态）并且在两个小时左右上层没有任何数据传输的情况下，这套机制才会被激活。
     *
     * ChannelOption.TCP_NODELAY, true
     *  在TCP/IP协议中，无论发送多少数据，总是要在数据前面加上协议头，同时，对方接收到数据，也需要发送ACK表示确认。为了尽可能的利用网络带宽，TCP总是希望尽可能的发送足够大的数据。这里就涉及到一个名为Nagle的算法，该算法的目的就是为了尽可能发送大块数据，避免网络中充斥着许多小数据块。
     *  TCP_NODELAY就是用于启用或关于Nagle算法。如果要求高实时性，有数据发送时就马上发送，就将该选项设置为true关闭Nagle算法；如果要减少发送次数减少网络交互，就设置为false等累积一定大小后再发送。默认为false。
     *
     * 4.ChannelOption.SO_REUSEADDR, true
     * SO_REUSEADDR允许启动一个监听服务器并捆绑其众所周知端口，即使以前建立的将此端口用做他们的本地端口的连接仍存在。这通常是重启监听服务器时出现，若不设置此选项，则bind时将出错。
     * SO_REUSEADDR允许在同一端口上启动同一服务器的多个实例，只要每个实例捆绑一个不同的本地IP地址即可。对于TCP，我们根本不可能启动捆绑相同IP地址和相同端口号的多个服务器。
     * SO_REUSEADDR允许单个进程捆绑同一端口到多个套接口上，只要每个捆绑指定不同的本地IP地址即可。这一般不用于TCP服务器。
     * SO_REUSEADDR允许完全重复的捆绑：当一个IP地址和端口绑定到某个套接口上时，还允许此IP地址和端口捆绑到另一个套接口上。一般来说，这个特性仅在支持多播的系统上才有，而且只对UDP套接口而言（TCP不支持多播）
     *
     * 5.ChannelOption.SO_RCVBUF  AND  ChannelOption.SO_SNDBUF
     * 定义接收或者传输的系统缓冲区buf的大小，
     *
     * 6.ChannelOption.ALLOCATOR
     * Netty4使用对象池，重用缓冲区
     * bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
     * bootstrap.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
     */

    public void start() {
        //创建两个线程组 boosGroup、workerGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //创建服务端的启动对象，设置参数
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            //设置两个线程组boosGroup和workerGroup
            serverBootstrap.group(bossGroup, workerGroup)
                    //设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //使用匿名内部类的形式初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //给pipeline管道设置处理器
                            // 心跳
                            socketChannel.pipeline().addLast(new ServerIdleHandler());
                            // 拆包
                            socketChannel.pipeline().addLast(new LengthFieldFrameDecoder());
                            // 解码
                            socketChannel.pipeline().addLast(new PackageCodecHandler());
                            socketChannel.pipeline().addLast(new ServerHandler());

                        }
                    });
            System.out.println("服务端准备就绪");
            //绑定端口号，启动服务端
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            System.out.println("绑定端口号，启动服务端");
            //对关闭通道进行监听
            channelFuture.channel().closeFuture().sync();
            System.out.println("通道关闭了");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
            System.out.println("shutdownGracefully");
        }
    }

    @Override
    public void run() {
        this.start();
    }
}
