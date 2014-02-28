package ssHookShot.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import ssHookShot.item.ItemMoveLeggings;

public class FuelPacket extends AbstractPacket
{
    int fuel;

    public FuelPacket() {}

    public FuelPacket(int fuel)
    {
        this.fuel = fuel;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(fuel);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        fuel = buffer.readInt();
    }

    @Override
    public void handleClientSide(EntityPlayer player) {}

    @Override
    public void handleServerSide(EntityPlayer player)
    {

        if(player.getCurrentArmor(2) != null&&player.getCurrentArmor(2).getItem() instanceof ItemMoveLeggings)
        {
            ItemMoveLeggings.set燃料(player.getCurrentArmor(2),fuel,player);
        }
    }
}
