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

    private KeyBinding keyMode = new KeyBinding("HookShot Mode", Keyboard.KEY_N, "HookShot");
    private KeyBinding keyRightAnchorShot = new KeyBinding("RightAnchorShot", Keyboard.KEY_B, "HookShot");
    private KeyBinding keyLeftAnchorShot = new KeyBinding("LeftAnchorShot", Keyboard.KEY_V, "HookShot");
    private KeyBinding keyRightAnchorRec = new KeyBinding("RightAnchorRec", Keyboard.KEY_X, "HookShot");
    private KeyBinding keyLeftAnchorRec = new KeyBinding("LeftAnchorRec", Keyboard.KEY_Z, "HookShot");
    private KeyBinding keyRightAnchorExtend = new KeyBinding("RightAnchorExtend", Keyboard.KEY_G, "HookShot");
    private KeyBinding keyLeftAnchorExtend = new KeyBinding("LeftAnchorExtend", Keyboard.KEY_H, "HookShot");
    private KeyBinding keyAnchorRec = new KeyBinding("AnchorRec", Keyboard.KEY_N, "HookShot");
    private KeyBinding keyOpenGUI = new KeyBinding("OpenGUI", Keyboard.KEY_F, "HookShot");
    private KeyBinding keyReload = new KeyBinding("Reload", Keyboard.KEY_R, "HookShot");
    private KeyBinding key = new KeyBinding("投げる", Keyboard.KEY_C, "HookShot");

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
        if (Keyboard.isKeyDown(keyRightAnchorRec.getKeyCode())) {
            keyData.add(DataManager.keyRightAnchorRec);
        }
        if (Keyboard.isKeyDown(keyLeftAnchorRec.getKeyCode())) {
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
            keyData.add(DataManager.key);
        }
        //闇コードここまで
        HookShot.packetPipeline.sendToServer(new KeyPacket((keyData)));
    }
}
