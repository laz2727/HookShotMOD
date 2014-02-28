package ssHookShot.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class EntityPullPacket extends AbstractPacket
{
    int eid;
    double x,y,z;

    public EntityPullPacket() {}

    public EntityPullPacket(int eid,double x,double y,double z)
    {
        this.eid = eid;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(eid);
        buffer.writeDouble(x);
        buffer.writeDouble(y);
        buffer.writeDouble(z);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        eid = buffer.readInt();
        x = buffer.readDouble();
        y = buffer.readDouble();
        z = buffer.readDouble();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {}

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        Entity e = player.worldObj.getEntityByID(eid);
        e.motionX = x;
        e.motionY = y;
        e.motionZ = z;
    }
}