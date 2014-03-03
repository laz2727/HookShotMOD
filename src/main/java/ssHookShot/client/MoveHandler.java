package ssHookShot.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import ssHookShot.item.ItemMoveLeggings;
import ssHookShot.system.DataManager;

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
                mx = x / 2;
                my = y / 2;
                mz = z / 2;

                if(DataManager.isKeyPress(mc.thePlayer, DataManager.keyLeftAnchorRec)||DataManager.isKeyPress(mc.thePlayer, DataManager.keyRightAnchorRec))
                {
                    mx += Math.cos(Math.toRadians(mc.thePlayer.rotationYaw + 90))*Math.cos(Math.toRadians(mc.thePlayer.rotationPitch));
                    mz += Math.sin(Math.toRadians(mc.thePlayer.rotationYaw + 90))*Math.cos(Math.toRadians(mc.thePlayer.rotationPitch));
                    my += -Math.cos(Math.toRadians(mc.thePlayer.rotationPitch - 90));
                }

                mc.thePlayer.motionX = mx;
                mc.thePlayer.motionY = my;
                mc.thePlayer.motionZ = mz;
                oldFlag = flag;
                flag = 0;
            } else if(oldFlag > 0&&flag == 0) {
                if(x == 0||y == 0||z == 0)
                {
                    mc.thePlayer.motionX = 0;
                    mc.thePlayer.motionY = 0;
                    mc.thePlayer.motionZ = 0;
                    mx = 0;
                    my = 0;
                    mz = 0;
                }
                else {
                    c = 40;
                }
                oldFlag = 0;
            } else if (c > 0) {
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
