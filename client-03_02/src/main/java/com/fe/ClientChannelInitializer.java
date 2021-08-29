package com.fe;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;

/**
 * @Description ClientChannelInitializer
 * @Author sosuke :-)
 * @Date 2021/8/25 21:51
 */
public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {
    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        // System.out.println("channel initialized! ch = "+ch);
        ch.pipeline().addLast(new ClientHandler());
    }
}
