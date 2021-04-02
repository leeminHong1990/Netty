package com.netty.client.handler;

import com.netty.packet.Body;
import com.netty.packet.Header;
import com.netty.packet.Packet;
import com.netty.packet.message.LoginRequest;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

import static com.netty.constant.Command.LOGIN_REQUEST;

/**
 * @author min
 */
@Sharable
@Slf4j
public class LoginHandler extends ChannelInboundHandlerAdapter {

    private String username;
    private String password;

    public LoginHandler(String username, String password) {
        this.username = username;
        this.password = password;
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("LoginHandler channelActive");
//        ctx.channel().writeAndFlush(getLoginRequestPacket());
//        System.out.println("发送 登录 消息！！！");
//        ctx.channel().writeAndFlush(Unpooled.copiedBuffer("歪比巴卜~茉莉~Are you good~马来西亚~", CharsetUtil.UTF_8));
        for (int i = 0; i < 200; i++) {
            Packet packet = getLoginRequestPacket();
            ctx.channel().writeAndFlush(packet);
        }


        log.info("发送 登录 消息！！！");
//        ctx.fireChannelActive();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("LoginHandler channelRead");
    }

    private Packet getLoginRequestPacket() {
        Header header = new Header();
        header.setCmd(LOGIN_REQUEST);

        LoginRequest  loginRequest = new LoginRequest(this.username, this.password);

        Body<LoginRequest> body = new Body(loginRequest);
        Packet packet = new Packet(header, body);
        return packet;
    }
}
