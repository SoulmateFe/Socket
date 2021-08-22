package com.fe.v1;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @Description SocketProcessor
 * @Author sosuke :-)
 * @Date 2021/8/22 11:21
 */
public class SocketProcessor implements Runnable {
    private Socket socket;
    public SocketProcessor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                InputStream inputStream = socket.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

                OutputStream outputStream = socket.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream));
        ) {
            // 读取客户端发送的数据
            String line = bufferedReader.readLine();
            System.out.println("接收到客户端 " +socket.getPort()+ " 数据："+line+"\n");
            // 给客户端响应结果
            // \n 表示流结束
            bufferedWriter.write("服务端对客户端 " +socket.getPort()+ " 的返回数据\n");
            bufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != socket) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
