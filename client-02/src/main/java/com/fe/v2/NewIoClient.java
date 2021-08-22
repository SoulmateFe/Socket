package com.fe.v2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description NewIoClient
 * @Author sosuke :-)
 * @Date 2021/8/22 20:53
 */
public class NewIoClient {
    public static void main(String[] args) {
        try (
                SocketChannel socketChannel = SocketChannel.open();
                Selector selector = Selector.open();
        ) {
            socketChannel.configureBlocking(false); // 设置非阻塞
            socketChannel.connect(new InetSocketAddress("localhost",8088));
            // 把连接事件注册到多路复用器上
            socketChannel.register(selector, SelectionKey.OP_CONNECT);

            while (true) {
                selector.select(); // 阻塞的
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    // 连接事件
                    if (key.isConnectable()) {
                        handleConnect(key);
                    } else if (key.isReadable()) { // 读就绪事件
                        handleRead(key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleConnect(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        if (socketChannel.isConnectionPending()) {
            socketChannel.finishConnect();
        }
        socketChannel.configureBlocking(false); // 设置非阻塞
        socketChannel.write(ByteBuffer.wrap("Hello Server, I'm NIO client.".getBytes()));
        // 注册读就绪事件
        socketChannel.register(key.selector(), SelectionKey.OP_READ);
    }

    private static void handleRead(SelectionKey key) throws IOException {
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int i = socketChannel.read(byteBuffer);
        if (i > 0) {
            System.out.println("收到服务端消息："+new String(byteBuffer.array()));
        }
    }
}
