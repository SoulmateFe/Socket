package com.fe;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * @Description NettyClient
 * @Author sosuke :-)
 * @Date 2021/8/24 22:09
 */
public class NettyClient {
    public static void main(String[] args) {
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
                    .handler(new ClientChannelInitializer());

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
}
