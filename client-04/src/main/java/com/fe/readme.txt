学习使用netty codec
1、定义TankMsg x, y
2、TankMsgEncoder负责编码
3、TankMsgDecoder负责解码
4、将Encoder加入客户端Channel处理链
5、将Decoder加入服务器Channel处理链
6、在客户端channelActive的时候发送一个TankMsg
7、观察服务器是否接收正确