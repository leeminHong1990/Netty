package com.netty.server.handler;

import com.netty.packet.Body;
import com.netty.packet.Header;
import com.netty.packet.Packet;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;


@Sharable
@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<Packet> {


    public void handleResponse() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelActive();
        System.out.println("ServerHandler channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        System.out.println("ServerHandler channelInactive");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        System.out.println("------------channelRead0---------------");
        Header header = msg.getHeader();
        Body body = msg.getBody();
        log.info("收到消息: cmd: {} message: {}", header.getCmd(), body.getMessage());
    }
}
