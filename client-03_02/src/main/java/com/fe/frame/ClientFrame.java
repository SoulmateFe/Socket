package com.fe.frame;

import com.fe.NettyClient;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @Description ClientFrame
 * @Author sosuke :-)
 * @Date 2021/8/26 23:10
 */
public class ClientFrame extends Frame {
    public static final ClientFrame INSTANCE = new ClientFrame();
    TextArea textArea = new TextArea();
    TextField textField = new TextField();
    NettyClient nettyClient;

    public ClientFrame() {
        this.setSize(600, 400);
        this.setLocation(100, 20);
        this.add(textArea, BorderLayout.CENTER);
        this.add(textField, BorderLayout.SOUTH);
        textArea.setEditable(false);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // 通知服务端要退出
                nettyClient.closeConnect();
                System.exit(0);
            }
        });
        textField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 发送给服务器
                nettyClient.send(textField.getText());
                // 清空文本框
                textField.setText("");
            }
        });
    }

    public static void main(String[] args) {
        ClientFrame clientFrame = ClientFrame.INSTANCE;
        clientFrame.setVisible(true);
        // 连接服务器
        clientFrame.connetToServer();
    }

    /**
     * 连接服务器
     */
    private void connetToServer() {
        nettyClient = new NettyClient();
        nettyClient.connect();
    }

    /**
     * 更新面板内容
     * @param acceptMsg
     */
    public void updateText(String acceptMsg) {
        String currentMsg = this.textArea.getText() + System.getProperty("line.separator") + acceptMsg;
        this.textArea.setText(currentMsg);
    }
}
