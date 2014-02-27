package ssHookShot.system;

import cpw.mods.fml.common.network.IGuiHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import ssHookShot.inventory.ContainerSouti;

public class CommonProxy implements IGuiHandler {
    public void register(){}

    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        if (ID == DataManager.moveLeggingsGUIID)
            return new ContainerSouti(player);

        return null;
    }

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z){
        return null;//何もしない
    }
}
