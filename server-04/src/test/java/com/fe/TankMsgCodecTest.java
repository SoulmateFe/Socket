package com.fe;

import com.fe.codec.TankMsgDecoder;
import com.fe.codec.TankMsgEncoder;
import com.fe.entity.TankMsg;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Assert;
import org.junit.Test;

/**
 * @Description TankMsgCodecTest
 * codec 测试
 * @Author sosuke :-)
 * @Date 2021/8/30 21:50
 */
public class TankMsgCodecTest {

    /**
     * Outbound 从左往右
     */
    @Test
    public void testEncoder() {
        TankMsg tankMsg = new TankMsg(4, 9);
        EmbeddedChannel channel = new EmbeddedChannel(new TankMsgEncoder());
        // 将tankMsg写到Outbound channel
        channel.writeOutbound(tankMsg); // tankMsg从左往右，满足encoder

        // 从Outbound channel读出ByteBuf
        ByteBuf buf = channel.readOutbound();
        int x = buf.readInt();
        int y = buf.readInt();
        Assert.assertTrue(x == 4 && y == 9);
        buf.release();
    }

    /**
     * Inbound 从右往左
     */
    @Test
    public void testEncoder2() {
        ByteBuf buffer = Unpooled.buffer();
        TankMsg tankMsg = new TankMsg(2, 8);
        buffer.writeInt(tankMsg.getX());
        buffer.writeInt(tankMsg.getY());

        EmbeddedChannel ch = new EmbeddedChannel(new TankMsgEncoder(), new TankMsgDecoder());
        // 将buffer写到Inbound channel
        ch.writeInbound(buffer); // buffer从右往左，满足decoder，转化成TankMsg；而encoder的右侧是buffer，不满足encoder

        // 从Inbound channel读出TankMsg
        TankMsg tank = ch.readInbound();
        Assert.assertTrue(tank.getX() == 2 && tank.getY() == 8);
    }

}
