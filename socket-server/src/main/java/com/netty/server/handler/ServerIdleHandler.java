package com.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.channel.ChannelHandler.Sharable;


@Sharable
public class ServerIdleHandler extends IdleStateHandler {
    private static int HEART_BEAT_TIME = 15;

    public ServerIdleHandler() {
        super(0, 0, 60);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        System.out.println(HEART_BEAT_TIME + "秒内没有收到心跳 关闭连接！");
        ctx.channel().close();
    }
}
