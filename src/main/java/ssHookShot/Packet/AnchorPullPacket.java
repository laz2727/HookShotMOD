package ssHookShot.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import ssHookShot.client.MoveHandler;

public class AnchorPullPacket extends AbstractPacket
{
    double x,y,z;
    int flag;

    public AnchorPullPacket() {}

    public AnchorPullPacket(double x, double y, double z, int flag) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.flag = flag;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
        buffer.writeInt(flag);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        MoveHandler.x = buffer.readDouble();
        MoveHandler.y = buffer.readDouble();
        MoveHandler.z = buffer.readDouble();
        MoveHandler.flag = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player){}

    @Override
    public void handleServerSide(EntityPlayer player) {}
}
