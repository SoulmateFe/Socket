package com.fe;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;

import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;

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
        // System.out.println("pipeline.addLast->ServerChildHandler->channelActive-->"+Thread.currentThread().getName() +"==="+ Thread.currentThread().getId());
        // 客户端连接上后将channel添加到group
        NettyServer.clients.add(ctx.channel());
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
            String acceptMsg = new String(bytes);
            // System.out.println("===>"+acceptMsg);

            String response = acceptMsg;
            SocketAddress clientAddress = ctx.channel().remoteAddress();
            if ("_bye_".equals(acceptMsg)) {
                response = clientAddress + " 客户端要求退出";
                ByteBuf buffer = Unpooled.copiedBuffer(response.getBytes(StandardCharsets.UTF_8));
                NettyServer.clients.writeAndFlush(buffer);
                NettyServer.clients.remove(ctx.channel());
                ctx.close();
            } else {
                // 写回给通道组中的所有客户端
                if (!acceptMsg.endsWith("已上线")) {
                    response = clientAddress.toString() +": "+ acceptMsg;
                }
                ByteBuf buffer = Unpooled.copiedBuffer(response.getBytes(StandardCharsets.UTF_8));
                NettyServer.clients.writeAndFlush(buffer); // 客户端read接收时强转成ByteBuf，所以这里要传ByteBuf
            }
        } finally { // 释放byteBuf
            if (null != byteBuf) {
                ReferenceCountUtil.release(byteBuf);
            }
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 删除有异常的客户端channel，并关闭连接
        NettyServer.clients.remove(ctx.channel());
        ctx.close();
    }
}
