package com.fe.v1;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @Description NewIoClient
 * @Author sosuke :-)
 * @Date 2021/8/22 20:53
 */
public class NewIoClient {
    public static void main(String[] args) {
        try (
                SocketChannel socketChannel = SocketChannel.open();
        ) {
            // socketChannel.configureBlocking(false); // 设置非阻塞
            socketChannel.connect(new InetSocketAddress("localhost",8088));

            ByteBuffer buffer = ByteBuffer.allocate(1024);
            // 向服务端发送数据
            buffer.put("hello, this is nio client".getBytes());
            buffer.flip();
            socketChannel.write(buffer);

            // 从服务端读取返回数据
            buffer.clear();
            int size = socketChannel.read(buffer);
            if (size > 0) {
                System.out.println("收到了服务端的数据："+ new String(buffer.array(),0,size));
            } else {
                System.out.println("没有收到数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
