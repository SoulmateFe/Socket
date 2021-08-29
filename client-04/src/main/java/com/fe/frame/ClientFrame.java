package com.fe.frame;

import com.fe.NettyClient;
import com.fe.entity.TankMsg;

import javax.lang.model.element.VariableElement;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;

/**
 * @Description ClientFrame
 * @Author sosuke :-)
 * @Date 2021/8/26 23:10
 */
public class ClientFrame extends Frame {
    public static final ClientFrame INSTANCE = new ClientFrame();
    TextArea textArea = new TextArea();
    Button btnSend = new Button("send");
    Button btnClear = new Button("clear");
    Random random = new Random();
    NettyClient nettyClient;

    public ClientFrame() {
        this.setSize(600, 400);
        this.setLocation(100, 20);
        this.add(textArea, BorderLayout.CENTER);
        Panel p = new Panel(new GridLayout(1, 2));
        p.add(btnSend);
        p.add(btnClear);
        this.add(p, BorderLayout.NORTH);
        this.setFont(new Font("verderna",Font.PLAIN, 18));
        textArea.setEditable(false);
        this.btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TankMsg tankMsg = new TankMsg(random.nextInt(10), random.nextInt(20));
                updateText("send content : "+tankMsg.toString());
                nettyClient.send(tankMsg);
            }
        });
        this.btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText("");
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
        nettyClient.connect("localhost", 8088);
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
