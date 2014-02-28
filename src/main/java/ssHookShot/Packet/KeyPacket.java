package ssHookShot.Packet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import ssHookShot.system.DataManager;

import java.util.ArrayList;
import java.util.List;

public class KeyPacket extends AbstractPacket
{
    private List<Integer> keyData = new ArrayList<Integer>();

    public KeyPacket() {}

    public KeyPacket(List<Integer> keys)
    {
        this.keyData = keys;
    }

    @Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        buffer.writeInt(keyData.size());

        for(int i = 0;i<keyData.size();i++)
        {
            buffer.writeInt(keyData.get(i));
        }
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf buffer) {
        int c = buffer.readInt();

        for(int i = 0;i<c;i++)
        {
            keyData.add(buffer.readInt());
        }
    }

    @Override
    public void handleClientSide(EntityPlayer player) {}

    @Override
    public void handleServerSide(EntityPlayer player)
    {
        DataManager.setKeyData(player, keyData);
    }
}
