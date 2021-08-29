package com.fe.handler;

import com.fe.entity.TankMsg;
import com.fe.frame.ClientFrame;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @Description ClientHandler
 * 发送TankMsg
 * @Author sosuke :-)
 * @Date 2021/8/29 19:37
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.writeAndFlush(new TankMsg(3,10));
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = null;
        try {
            // 从服务端读取
            byteBuf = (ByteBuf) msg;
            byte[] bytes = new byte[byteBuf.readableBytes()];
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
            String acceptMsg = new String(bytes);

            // 将服务端返回的信息显示在面板上
            ClientFrame.INSTANCE.updateText(acceptMsg);
        } finally { // 释放byteBuf
            if (null != byteBuf) {
                ReferenceCountUtil.release(byteBuf);
            }
        }
    }

}
