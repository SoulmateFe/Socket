package com.fe;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.StandardCharsets;

/**
 * @Description NettyClient
 * @Author sosuke :-)
 * @Date 2021/8/24 22:09
 */
public class NettyClient {
    private Channel channel;

    public static void main(String[] args) {
        new NettyClient().connect();
    }

    public void connect() {
        // 线程池
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup(1);
        // 辅助启动类
        Bootstrap bootstrap = new Bootstrap();

        try {
                    // 指定线程池
            bootstrap.group(eventLoopGroup)
                    // 指定channel，可以是阻塞的，也可以是非阻塞的
                    .channel(NioSocketChannel.class)
                    // channel发生事件时的处理方法
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            // System.out.println("channel initialized! ch = "+ch);
                            ch.pipeline().addLast(new ClientHandler());
                        }
                    });

            System.out.println("start to connect...");
            // 这里是异步的
            ChannelFuture channelFuture = bootstrap.connect("localhost", 8088);
            //.sync();

            /**
             *
             Adds the specified listener to this future.
             The specified listener is notified when this future is done.
             If this future is already completed, the specified listener is notified immediately.
             */
            channelFuture.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if (!future.isSuccess()) {
                        System.out.println("not connected!");
                    } else {
                        System.out.println("connected!");
                        // channel初始化完成后保存
                        channel = future.channel();
                    }
                }
            });
            channelFuture.sync(); // 同步等待结果
            // closeFuture会阻塞等待close方法的调用
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }

    public void send(String msg) {
        ByteBuf byteBuf = Unpooled.copiedBuffer(msg.getBytes(StandardCharsets.UTF_8));
        channel.writeAndFlush(byteBuf);
    }

    public void closeConnect() {
        send("_bye_");
    }
}
