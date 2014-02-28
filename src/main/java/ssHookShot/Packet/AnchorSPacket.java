package ssHookShot.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import ssHookShot.Entity.EntityAnchor;
import ssHookShot.item.ItemMoveLeggings;

public class AnchorSPacket extends AbstractPacket
{
    int eid;
    int side;

    public AnchorSPacket() {}

    public AnchorSPacket(int fuel, int side)
    {
        this.eid = fuel;
        this.side = side;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(eid);
        buffer.writeInt(side);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        eid = buffer.readInt();
        side = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player)
    {
        if (side == 0) {
            ItemMoveLeggings.rightAnchorMap.remove(player);
            if(eid != -1)
            ItemMoveLeggings.rightAnchorMap.put(player, (EntityAnchor) player.worldObj.getEntityByID(eid));
        } else if (side == 1) {
            ItemMoveLeggings.leftAnchorMap.remove(player);
            if(eid != -1)
            ItemMoveLeggings.leftAnchorMap.put(player, (EntityAnchor) player.worldObj.getEntityByID(eid));
        }
    }

    @Override
    public void handleServerSide(EntityPlayer player) {}
}
