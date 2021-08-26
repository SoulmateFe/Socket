package com.fe;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

/**
 * @Description ServerChildHandler
 * ChannelInboundHandlerAdapter 把读数据的方法做了骨架式的实现
 * 这个不是adapter模式，是对接口方法的空实现，这样我们使用时可以只实现自己需要的方法即可
 * @Author sosuke :-)
 * @Date 2021/8/25 21:34
 */
public class ServerChildHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("pipeline.addLast->ServerChildHandler->channelActive-->"+Thread.currentThread().getName() +"==="+ Thread.currentThread().getId());
    }

    /**
     * client端在channelActive中writeAndFlush后，会自动调用这个方法
     * @param ctx
     * @param msg client写的ByteBuf
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = null;
        try {
            // 从客户端读取
            byteBuf = (ByteBuf) msg;
            // 定义字节数组，长度为byteBuf.readableBytes()
            byte[] bytes = new byte[byteBuf.readableBytes()];
            // 从byteBuf.readerIndex()开始读，读到bytes中
            byteBuf.getBytes(byteBuf.readerIndex(), bytes);
            System.out.println("读取客户端："+new String(bytes));
            // System.out.println(byteBuf.toString(CharsetUtil.UTF_8));
            // 写回给客户端，会自动释放byteBuf
            ctx.writeAndFlush(msg);

            // System.out.println("byteBuf = " + byteBuf);
            // System.out.println("byteBuf.refCnt() = " + byteBuf.refCnt());
        } finally { // 释放byteBuf
            // if (null != byteBuf) {
            //     ReferenceCountUtil.release(byteBuf);
            // }
            // System.out.println("byteBuf.refCnt() = " + byteBuf.refCnt());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
    }
}
