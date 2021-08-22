package com.fe.v2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @Description SocketProcessorV2
 * @Author sosuke :-)
 * @Date 2021/8/22 18:53
 */
public class SocketProcessorV2 implements Runnable{
    private Socket socket;

    public SocketProcessorV2(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                // 用于读取客户端消息
                BufferedReader socketIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                // 用于向客户端发送消息
                PrintWriter socketOut = new PrintWriter(socket.getOutputStream());
                // 用于读取键盘输入
                BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in));
        ) {
            // 读取客户端的消息
            System.out.println("Client["+socket.getPort()+"]：" + socketIn.readLine());
            String line = sysIn.readLine();
            while (!"bye".equals(line)) {
                socketOut.println(line);
                socketOut.flush(); // 向客户端回消息
                System.out.println("Server["+socket.getPort()+"]：" + line);
                System.out.println("Client["+socket.getPort()+"]：" + socketIn.readLine());
                line = sysIn.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
