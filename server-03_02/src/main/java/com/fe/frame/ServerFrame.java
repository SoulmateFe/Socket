package com.fe.frame;

import com.fe.NettyServer;

import java.awt.*;
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
    NettyServer nettyServer;

    public ServerFrame() {
        this.setSize(1600, 600);
        this.setLocation(300, 30);
        taLeft.setEditable(false);
        taRight.setEditable(false);
        Panel p = new Panel(new GridLayout(1, 2));
        p.add(taLeft);
        p.add(taRight);
        this.add(p);
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
