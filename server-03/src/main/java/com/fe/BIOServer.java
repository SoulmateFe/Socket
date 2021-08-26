package com.fe;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description Server
 * BIO测试类
 * @Author sosuke :-)
 * @Date 2021/8/24 22:17
 */
public class BIOServer {

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8088);
        Socket socket = serverSocket.accept();
        System.out.println("a client connected!");
        while (true) {

        }
    }
}
