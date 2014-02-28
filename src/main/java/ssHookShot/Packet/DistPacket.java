package ssHookShot.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import ssHookShot.Entity.EntityAnchor;
import ssHookShot.item.ItemMoveLeggings;

public class DistPacket extends AbstractPacket
{
    double dist;
    int side;

    public DistPacket() {}

    public DistPacket(double fuel, int side)
    {
        this.dist = fuel;
        this.side = side;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeDouble(dist);
        buffer.writeInt(side);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        dist = buffer.readDouble();
        side = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {}

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        Entity e = null;

        if (side == 0) {
            if (ItemMoveLeggings.rightAnchorMap.containsKey(player)) {
                e = ItemMoveLeggings.rightAnchorMap.get(player);
            }
        } else if (side == 1) {
            if (ItemMoveLeggings.leftAnchorMap.containsKey(player)) {
                e = ItemMoveLeggings.leftAnchorMap.get(player);
            }
        }

        if (e != null) {
            if(dist != 0)
                ((EntityAnchor) e).dist += dist;
            else
            {
                ((EntityAnchor) e).dist = player.getDistanceToEntity(e);
            }
        }
    }
}
