package com.fe;

import com.fe.codec.TankMsgDecoder;
import com.fe.codec.TankMsgEncoder;
import com.fe.frame.ServerFrame;
import com.fe.handler.ServerChildHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Description NettyServer
 * @Author sosuke :-)
 * @Date 2021/8/25 06:34
 */
public class NettyServer {
    // 使用单线程处理通道组中的事件
    public static ChannelGroup clients = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public void start() {
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
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ServerFrame.INSTANCE.updateServerMsg("channel initialized! ch = " + ch);
                            // System.out.println("childHandler->ServerInitializer->initChannel-->"+Thread.currentThread().getName() +"==="+ Thread.currentThread().getId());
                            ChannelPipeline pipeline = ch.pipeline();
                            // 编码解码要放在ServerChildHandler之前
                            pipeline.addLast(new TankMsgEncoder())
                                    .addLast(new TankMsgDecoder())
                                    .addLast(new ServerChildHandler());
                        }
                    })
                    // 监听8088
                    .bind(8088)
                    // 同步等待启动成功
                    .sync();
            ServerFrame.INSTANCE.updateServerMsg("server started!");

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
