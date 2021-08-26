package com.fe;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;

/**
 * @Description ServerInitializer
 * @Author sosuke :-)
 * @Date 2021/8/25 21:32
 */
public class ServerInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        System.out.println("channel initialized! ch = " + ch);
        System.out.println("childHandler->ServerInitializer->initChannel-->"+Thread.currentThread().getName() +"==="+ Thread.currentThread().getId());
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast(new ServerChildHandler());
    }
}
