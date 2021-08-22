package com.fe.v1;

import javax.lang.model.element.VariableElement;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

/**
 * @Description NewIoServer
 * @Author sosuke :-)
 * @Date 2021/8/22 20:44
 */
public class NewIoServer {
    public static void main(String[] args) {
        try (
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        ) {
            serverSocketChannel.configureBlocking(false); // 设置非阻塞
            serverSocketChannel.socket().bind(new InetSocketAddress(8088));
            while (true) {
                // 监听客户端请求，非阻塞的
                SocketChannel socketChannel = serverSocketChannel.accept();
                if (null != socketChannel) {
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    // 读取客户端数据
                    socketChannel.read(buffer); // 读取到缓冲区
                    System.out.println("收到了客户端数据："+new String(buffer.array()));
                    // 向客户端返回数据
                    buffer.flip();
                    // buffer.put("this is nio server response".getBytes());
                    socketChannel.write(buffer);
                } else {
                    TimeUnit.SECONDS.sleep(1);
                    System.out.println("连接未就绪");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
