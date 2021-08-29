package com.fe.frame;

import com.fe.NettyServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @Description ServerFrame
 * @Author sosuke :-)
 * @Date 2021/8/29 17:31
 */
public class ServerFrame extends Frame {
    public static final ServerFrame INSTANCE = new ServerFrame();
    TextArea taLeft = new TextArea();
    TextArea taRight = new TextArea();
    Button leftBtnClear = new Button("clear");
    Button rightBtnClear = new Button("clear");
    NettyServer nettyServer;

    public ServerFrame() {
        this.setSize(1600, 600);
        this.setLocation(300, 30);
        taLeft.setEditable(false);
        taRight.setEditable(false);
        Panel p1 = new Panel(new GridLayout(1, 2));
        p1.add(leftBtnClear);
        p1.add(rightBtnClear);
        Panel p2 = new Panel(new GridLayout(1, 2));
        p2.add(taLeft);
        p2.add(taRight);
        this.setFont(new Font("verderna",Font.PLAIN, 18));
        this.add(p1,BorderLayout.NORTH);
        this.add(p2,BorderLayout.CENTER);
        this.leftBtnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taLeft.setText("");
            }
        });
        this.rightBtnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                taRight.setText("");
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        ServerFrame serverFrame = ServerFrame.INSTANCE;
        serverFrame.setVisible(true);
        serverFrame.startServer();
    }

    private void startServer() {
        nettyServer = new NettyServer();
        nettyServer.start();
    }

    public void updateServerMsg(String msg) {
        this.taLeft.setText(taLeft.getText() + msg + System.getProperty("line.separator"));
    }

    public void updateClientMsg(String msg) {
        this.taRight.setText(taRight.getText() + msg + System.getProperty("line.separator"));
    }
}
