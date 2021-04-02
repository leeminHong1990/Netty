package com.netty.client;

import com.netty.client.handler.ClientHandler;
import com.netty.client.handler.LoginHandler;
import com.netty.handler.LengthFieldFrameDecoder;
import com.netty.handler.PackageCodecHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @author min
 */
public class TCPClient {

    private static final String host = "localhost";
    private static final int port = 8888;

    private String username = "min";
    private String password = "12345678";

    private static volatile TCPClient instance;

    public static TCPClient getSingletonInstance() {
        if (instance == null) {
            synchronized (TCPClient.class) {
                if (instance == null) {
                    instance = new TCPClient();
                }
            }
        }
        return instance;
    }


    public void login(String username, String password) {
        this.username = username;
        this.password = password;

        EventLoopGroup eventExecutors = new NioEventLoopGroup();
        try {
            //创建bootstrap对象，配置参数
            Bootstrap bootstrap = new Bootstrap();
            //设置线程组
            bootstrap.group(eventExecutors)
                    //设置客户端的通道实现类型
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {

                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //添加客户端通道的处理器 -- 责任链模式
                            socketChannel.pipeline().addLast(new ClientHandler());
                            // 拆包
                            socketChannel.pipeline().addLast(new LengthFieldFrameDecoder());
                            socketChannel.pipeline().addLast(new LoginHandler(username, password));
                            // 自定义了解码器，由于没有结束符，必须主动encode,否则会导致服务端channelRead0一直不会执行
                            socketChannel.pipeline().addLast(new PackageCodecHandler());

                        }
                    });
            //连接服务端
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            System.out.println("连接服务端");
            //对通道关闭进行监听
            channelFuture.channel().closeFuture().sync();
            System.out.println("与服务端断开连接");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("连接服务器失败");
        } finally {
            //关闭线程组
            eventExecutors.shutdownGracefully();
            System.out.println("关闭线程组");
        }
    }
}
