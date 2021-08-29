package com.fe.codec;

import com.fe.entity.TankMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Description TankMsgEncoder 负责编码
 * @Author sosuke :-)
 * @Date 2021/8/29 19:12
 */
public class TankMsgEncoder extends MessageToByteEncoder<TankMsg> {
    @Override
    protected void encode(ChannelHandlerContext ctx, TankMsg msg, ByteBuf out) throws Exception {
        out.writeInt(msg.getX());
        out.writeInt(msg.getY());
    }
}
