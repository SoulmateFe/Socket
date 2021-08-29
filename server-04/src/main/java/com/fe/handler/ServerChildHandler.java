package com.fe.handler;

import com.fe.NettyServer;
import com.fe.entity.TankMsg;
import com.fe.frame.ServerFrame;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
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
        ServerFrame.INSTANCE.updateServerMsg("channel active");
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
        try {
            ServerFrame.INSTANCE.updateServerMsg("channel read");
            TankMsg tankMsg = (TankMsg) msg;
            String req = tankMsg.toString();
            SocketAddress remoteAddress = ctx.channel().remoteAddress();
            ServerFrame.INSTANCE.updateClientMsg(remoteAddress.toString() +": "+ req);

            // 写回给客户端
            // ByteBuf buffer = Unpooled.copiedBuffer(req.getBytes(StandardCharsets.UTF_8));
            // NettyServer.clients.writeAndFlush(buffer); // 客户端read接收时强转成ByteBuf，所以这里要传ByteBuf

            // 写回给客户端，使用编码器处理TankMsg
            NettyServer.clients.writeAndFlush(tankMsg);

        } finally { // 释放refCount
            ReferenceCountUtil.release(msg);
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
