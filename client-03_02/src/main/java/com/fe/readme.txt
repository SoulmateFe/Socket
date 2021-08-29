使用netty编写客户端，实现群聊，即客户端向服务端发送消息，服务端将消息群发给所有登录的客户端
1、窗口显示完毕后调用new NettyClient().connect();
2、客户端channel初始化完成后，保存channel变量，后续使用此channel发送数据
    2-1、封装发送数据的send(String msg) 方法
3、当Client接收到服务端的信息时，通过调用ClientFrame.INSTANCE.updateText(String msg) 更新面板内容
4、客户端优雅关闭
    4-1、通知服务端要退出，发送"_bye_"
    4-2、服务端收到特定消息处理
    4-3、服务端移除channel并关闭ctx