package ssHookShot.client;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import ssHookShot.HookShot;
import ssHookShot.Packet.KeyPacket;
import ssHookShot.system.DataManager;

import java.util.ArrayList;
import java.util.List;

public class KeyHandler {

    private static Minecraft mc = FMLClientHandler.instance().getClient();

    private KeyBinding keyMode = new KeyBinding("key.hookshotMode", Keyboard.KEY_N, "HookShot");
    private KeyBinding keyRightAnchorShot = new KeyBinding("key.rightAnchorShot", Keyboard.KEY_B, "HookShot");
    private KeyBinding keyLeftAnchorShot = new KeyBinding("key.leftAnchorShot", Keyboard.KEY_V, "HookShot");
    private KeyBinding keyRightAnchorRec = new KeyBinding("key.rightAnchorRec", Keyboard.KEY_X, "HookShot");
    private KeyBinding keyLeftAnchorRec = new KeyBinding("key.leftAnchorRec", Keyboard.KEY_Z, "HookShot");
    private KeyBinding keyRightAnchorExtend = new KeyBinding("key.rightAnchorExtend", Keyboard.KEY_G, "HookShot");
    private KeyBinding keyLeftAnchorExtend = new KeyBinding("key.leftAnchorExtend", Keyboard.KEY_H, "HookShot");
    private KeyBinding keyAnchorRec = new KeyBinding("key.anchorRec", Keyboard.KEY_Y, "HookShot");
    private KeyBinding keyOpenGUI = new KeyBinding("key.openGUI", Keyboard.KEY_F, "HookShot");
    private KeyBinding keyReload = new KeyBinding("key.reload", Keyboard.KEY_R, "HookShot");
    private KeyBinding key = new KeyBinding("key.throw", Keyboard.KEY_C, "HookShot");

    public KeyHandler() {
        ClientRegistry.registerKeyBinding(keyMode);
        ClientRegistry.registerKeyBinding(keyRightAnchorShot);
        ClientRegistry.registerKeyBinding(keyLeftAnchorShot);
        ClientRegistry.registerKeyBinding(keyRightAnchorRec);
        ClientRegistry.registerKeyBinding(keyLeftAnchorRec);
        ClientRegistry.registerKeyBinding(keyRightAnchorExtend);
        ClientRegistry.registerKeyBinding(keyLeftAnchorExtend);
        ClientRegistry.registerKeyBinding(keyAnchorRec);
        ClientRegistry.registerKeyBinding(keyOpenGUI);
        ClientRegistry.registerKeyBinding(keyReload);
        ClientRegistry.registerKeyBinding(key);
    }

    @SubscribeEvent
    public void key(TickEvent.ClientTickEvent event) {

        if (mc.thePlayer != null&&event.phase.equals(TickEvent.Phase.END)) {
            List<Integer> keyData = new ArrayList<Integer>();
            //闇コード
            if (keyMode.isPressed()) {
                keyData.add(DataManager.keyMode);
            }
            if (keyRightAnchorShot.isPressed()) {
                keyData.add(DataManager.keyRightAnchorShot);
            }
            if (keyLeftAnchorShot.isPressed()) {
                keyData.add(DataManager.keyLeftAnchorShot);
            }
            if (keyRightAnchorRec.getIsKeyPressed()) {
                keyData.add(DataManager.keyRightAnchorRec);
            }
            if (keyLeftAnchorRec.getIsKeyPressed()) {
                keyData.add(DataManager.keyLeftAnchorRec);
            }
            if (keyRightAnchorExtend.isPressed()) {
                keyData.add(DataManager.keyRightAnchorExtend);
            }
            if (keyLeftAnchorExtend.isPressed()) {
                keyData.add(DataManager.keyLeftAnchorExtend);
            }
            if (keyAnchorRec.isPressed()) {
                keyData.add(DataManager.keyAnchorRec);
            }
            if (keyOpenGUI.isPressed()) {
                keyData.add(DataManager.keyOpenGUI);
            }
            if (keyReload.isPressed()) {
                keyData.add(DataManager.keyReload);
            }
            if (key.isPressed()) {
                keyData.add(DataManager.keyThrow);
            }
            //闇コードここまで
            DataManager.setKeyData(mc.thePlayer, keyData);

            HookShot.packetPipeline.sendToServer(new KeyPacket((keyData)));
        }
    }
}
