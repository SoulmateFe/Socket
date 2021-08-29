package com.fe.codec;

import com.fe.entity.TankMsg;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @Description TankMsgDecoder
 * @Author sosuke :-)
 * @Date 2021/8/29 19:42
 */
public class TankMsgDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        // TCP 拆包 粘包的问题
        if (in.readableBytes() < 8) {
            return;
        }
        int x = in.readInt(); // 先写的x，所以先读的也是x
        int y = in.readInt();
        out.add(new TankMsg(x, y));
    }
}
