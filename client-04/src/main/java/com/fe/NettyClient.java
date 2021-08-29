package com.fe;

import com.fe.codec.TankMsgDecoder;
import com.fe.codec.TankMsgEncoder;
import com.fe.entity.TankMsg;
import com.fe.handler.ClientHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Description NettyClient
 * 发送一个TankMsg对象
 * @Author sosuke :-)
 * @Date 2021/8/29 19:25
 */
public class NettyClient {
    private Channel channel;
    public void connect(String host, int port) {
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        try {
            ChannelFuture future = bootstrap.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            // 编码解码要放在ClientHandler之前
                            pipeline.addLast(new TankMsgEncoder())
                                    .addLast(new TankMsgDecoder())
                                    .addLast(new ClientHandler());
                        }
                    })
                    .connect(host, port)
                    .sync();
            future.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (future.isSuccess()) {
                        channel = future.channel();
                    }
                }
            });
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    public void send(TankMsg tankMsg) {
        channel.writeAndFlush(tankMsg);
    }

}
