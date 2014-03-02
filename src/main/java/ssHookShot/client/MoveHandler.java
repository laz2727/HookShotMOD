package ssHookShot.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import ssHookShot.item.ItemMoveLeggings;

public class MoveHandler {
    private static Minecraft mc = FMLClientHandler.instance().getClient();

    public static double x = 0;
    public static double y = 0;
    public static double z = 0;
    public static double mx = 0;
    public static double my = 0;
    public static double mz = 0;
    public static int oldFlag = 0;
    public static int flag = 0;
    public static int c = 0;

    @SubscribeEvent
    public void key(TickEvent.PlayerTickEvent event) {

        if (mc.thePlayer.equals(event.player) && event.phase.equals(TickEvent.Phase.START)) {
            if (flag > 0) {
                mx += x / 5;
                my += y / 5;
                mz += z / 5;
                mc.thePlayer.motionX = mx;
                mc.thePlayer.motionY = my;
                mc.thePlayer.motionZ = mz;
                oldFlag = flag;
                flag = 0;
            } else if(oldFlag > 0&&flag == 0) {
                if(x == 0||y == 0||z == 0)
                {
                    c = 0;
                }
                else c = 40;
                oldFlag = 0;
            }
            if (c > 0) {
                c--;
                mc.thePlayer.motionX = mx;
                mc.thePlayer.motionY = my;
                mc.thePlayer.motionZ = mz;
                mx *= 0.975;
                my *= 0.975;
                mz *= 0.975;
            } else if(c == 0){
                mx = 0;
                my = 0;
                mz = 0;
            }
        }
    }
}
