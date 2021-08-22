package com.fe.v2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description SocketServerV1
 * TCP实现双向通信
 * @Author sosuke :-)
 * @Date 2021/8/22 17:19
 */
public class SocketServerV2 {
    static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        try (
                ServerSocket serverSocket = new ServerSocket(8088);
        ) {
            while (true) {
                // 阻塞等待客户端请求
                Socket socket = serverSocket.accept();
                executorService.submit(new SocketProcessorV2(socket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
