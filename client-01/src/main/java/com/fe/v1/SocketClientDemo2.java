package com.fe.v1;

import java.io.*;
import java.net.Socket;

/**
 * @Description SocketClientDemo
 * @Author sosuke :-)
 * @Date 2021/8/22 09:13
 */
public class SocketClientDemo2 {
    public static void main(String[] args) {

        try (
                Socket socket = new Socket("localhost", 8088);
                OutputStream outputStream = socket.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));

                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        ) {
            System.out.println("客户端请求连接，端口："+socket.getLocalPort());
            // \n 表示流结束
            bufferedWriter.write("hello, this is client socket-02\n");
            bufferedWriter.flush();

            String line = bufferedReader.readLine();
            System.out.println("接收到服务端数据："+line);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
