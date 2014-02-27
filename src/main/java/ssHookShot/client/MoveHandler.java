package ssHookShot.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;

public class MoveHandler {
    private static Minecraft mc = FMLClientHandler.instance().getClient();

    public static double x  = -1;
    public static double y  = -1;
    public static double z  = -1;

    @SubscribeEvent
    public void key(TickEvent.PlayerTickEvent event) {
        if (mc.thePlayer.equals(event.player)&&x != -1) {
            mc.thePlayer.motionX = x;
            mc.thePlayer.motionY = y;
            mc.thePlayer.motionZ = z;

            x = -1;
            y = -1;
            z = -1;
        }
    }
}
