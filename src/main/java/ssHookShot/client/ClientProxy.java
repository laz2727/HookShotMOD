package ssHookShot.client;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;
import ssHookShot.Entity.EntityAnchor;
import ssHookShot.Entity.EntityKenn;
import ssHookShot.HookShot;
import ssHookShot.client.gui.GuiMoveLeggings;
import ssHookShot.client.model.ModelKenn;
import ssHookShot.client.model.ModelMoveLeggings;
import ssHookShot.client.render.RenderAnchor;
import ssHookShot.client.render.RenderHa;
import ssHookShot.system.CommonProxy;
import ssHookShot.system.DataManager;

public class ClientProxy extends CommonProxy {

    public static ModelMoveLeggings moveLegModel = new ModelMoveLeggings();
    public static ModelKenn 剣モデル = new ModelKenn();

    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z)
    {
        if (ID == DataManager.moveLeggingsGUIID)
            return new GuiMoveLeggings(player);

        return null;
    }

    @Override
    public void register(){
        FMLCommonHandler.instance().bus().register(new KeyHandler());
        FMLCommonHandler.instance().bus().register(new MoveHandler());

        MinecraftForgeClient.registerItemRenderer(HookShot.instance.剣, (IItemRenderer) HookShot.instance.剣);

        RenderingRegistry.registerEntityRenderingHandler(EntityAnchor.class, new RenderAnchor());
        RenderingRegistry.registerEntityRenderingHandler(EntityKenn.class, new RenderHa());
    }
}
