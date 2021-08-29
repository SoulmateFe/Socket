package com.fe;

import com.fe.frame.ClientFrame;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @Description ClientHandler
 * ChannelInboundHandlerAdapter 把读数据的方法做了骨架式的实现
 * 这个不是adapter模式，是对接口方法的空实现，这样我们使用时可以只实现自己需要的方法即可
 * @Author sosuke :-)
 * @Date 2021/8/25 21:34
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // System.out.println("pipeline.addLast->ClientHandler->channelActive-->"+Thread.currentThread().getName() +"==="+ Thread.currentThread().getId());
        // 连接可用时，写出字符串，使用ByteBuf（Direct memory 直接访问内存，效率高）
        String msg = ctx.channel().localAddress() + "已上线";
        ByteBuf byteBuf = Unpooled.copiedBuffer(msg.getBytes());
        ctx.writeAndFlush(byteBuf); // 这里会自动释放内存
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = null;
        try {
            // 从服务端读取
            byteBuf = (ByteBuf) msg;
            // 定义字节数组，长度为byteBuf.readableBytes()
            byte[] bytes = new byte[byteBuf.readableBytes()];
            // 从byteBuf.readerIndex()开始读，读到bytes中
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
            String acceptMsg = new String(bytes);
            // System.out.println("===>"+acceptMsg);

            // 将服务端返回的信息显示在面板上
            ClientFrame.INSTANCE.updateText(acceptMsg);
        } finally { // 释放byteBuf
            if (null != byteBuf) {
                ReferenceCountUtil.release(byteBuf);
            }
        }
    }
}
