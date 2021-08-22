package com.fe.v1;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description ServerSocketDemo
 * @Author sosuke :-)
 * @Date 2021/8/22 09:05
 */
public class ServerSocketDemo {
    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try (
                // 监听端口
                ServerSocket serverSocket = new ServerSocket(8088);
        ) {
            while (true) {
                // 连接阻塞
                Socket socket = serverSocket.accept();
                System.out.println("客户端端口："+socket.getPort()+ " 已连接");
                executorService.submit(new SocketProcessor(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
