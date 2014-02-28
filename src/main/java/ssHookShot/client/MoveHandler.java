package ssHookShot.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;

public class MoveHandler {
    private static Minecraft mc = FMLClientHandler.instance().getClient();

    public static double x  = 0;
    public static double y  = 0;
    public static double z  = 0;
    public static int flag  = 0;

    @SubscribeEvent
    public void key(TickEvent.PlayerTickEvent event) {

        if(mc.thePlayer.equals(event.player)&&event.phase.equals(TickEvent.Phase.START)&&flag > 0){
            mc.thePlayer.motionX = x;
            mc.thePlayer.motionY = y;
            mc.thePlayer.motionZ = z;
            flag = 0;
        }
    }
}
