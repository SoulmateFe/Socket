package com.fe;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.EventExecutorGroup;

/**
 * @Description NettyServer
 * @Author sosuke :-)
 * @Date 2021/8/25 06:34
 */
public class NettyServer {
    public static void main(String[] args) {
        // 负责处理连接
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        // 负责处理socket上产生的事件
        EventLoopGroup workerGroup = new NioEventLoopGroup(2);
        // 辅助启动类
        ServerBootstrap bootstrap = new ServerBootstrap();

        try {
                    // 指定线程池
            ChannelFuture channelFuture = bootstrap.group(bossGroup, workerGroup)
                    // 指定channel类型
                    .channel(NioServerSocketChannel.class)
                    // 客户端连接上之后的处理方法
                    .childHandler(new ServerInitializer())
                    // 监听8088
                    .bind(8088)
                    // 同步等待启动成功
                    .sync();
            System.out.println("server started!");

            // closeFuture会阻塞等待close方法的调用
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally { // 优雅关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }

    }

}
