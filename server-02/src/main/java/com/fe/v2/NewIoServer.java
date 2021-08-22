package com.fe.v2;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;

/**
 * @Description NewIoServer
 * @Author sosuke :-)
 * @Date 2021/8/22 20:44
 */
public class NewIoServer {
    public static void main(String[] args) {
        try (
                ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
                // selector 必须是非阻塞
                Selector selector = Selector.open();
        ) {
            serverSocketChannel.configureBlocking(false); // 设置非阻塞
            serverSocketChannel.socket().bind(new InetSocketAddress(8088));
            // 把连接事件注册到多路复用器上
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            while (true) {
                selector.select(); // 阻塞机制
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    // 连接事件
                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) { // 读就绪事件
                        handleRead(key);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept(); // 这里一定会有一个连接
        socketChannel.configureBlocking(false); // 设置非阻塞
        socketChannel.write(ByteBuffer.wrap("Hello Client, I'm NIO server.".getBytes()));
        // 在socketChannel注册读事件，当下次轮询到读事件时处理
        socketChannel.register(key.selector(), SelectionKey.OP_READ);
    }

    private static void handleRead(SelectionKey key) throws IOException {
        // 上面注册的是socketChannel，所以这里是socketChannel
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int size = socketChannel.read(byteBuffer);// 这里一定有数据
        System.out.println("收到客户端消息："+new String(byteBuffer.array(),0,size));
    }


}
