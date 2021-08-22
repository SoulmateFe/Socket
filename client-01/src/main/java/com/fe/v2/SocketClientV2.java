package com.fe.v2;

import java.io.*;
import java.net.Socket;

/**
 * @Description SocketClientV1
 * TCP实现双向通信
 * @Author sosuke :-)
 * @Date 2021/8/22 17:18
 */
public class SocketClientV2 {
    public static void main(String[] args) {
        try (
                // 请求连接
                Socket socket = new Socket("localhost", 8088);
                // 用于读取键盘输入
                BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
                // 用于向服务端发送消息
                PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
                // 用于读取服务端消息
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        ) {
            String line = sysIn.readLine();
            while (!"bye".equals(line)) {
                socketOut.println(line);
                socketOut.flush(); // 向服务端发送消息
                System.out.println("Client["+socket.getLocalPort()+"]："+line);
                // 读取服务端消息
                System.out.println("Server："+socketIn.readLine());
                line = sysIn.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
