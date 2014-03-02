package ssHookShot.system;

import net.minecraft.entity.player.EntityPlayer;

import java.util.HashMap;
import java.util.List;
import java.util.WeakHashMap;

public class DataManager {//ハードコーディング大量の闇
    public static final String modeNone = "none";
    public static final String modeAuto = "auto";
    public static final String modeManual = "manual";

    public static final int right = 0;
    public static final int left = 1;

    public static final int keyMode = 1;
    public static final int keyRightAnchorShot = 2;
    public static final int keyLeftAnchorShot = 3;
    public static final int keyRightAnchorRec = 4;
    public static final int keyLeftAnchorRec = 5;
    public static final int keyRightAnchorExtend = 6;
    public static final int keyLeftAnchorExtend = 7;
    public static final int keyAnchorRec = 8;
    public static final int keyOpenGUI = 9;
    public static final int keyReload = 10;
    public static final int keyThrow = 11;

    public static int moveLeggingsGUIID = 0;

    private static HashMap<EntityPlayer, List<Integer>> KeyData = new HashMap<EntityPlayer, List<Integer>>();
    public static HashMap<EntityPlayer, String> PlayerMode = new HashMap<EntityPlayer, String>();

    public static void setKeyData(EntityPlayer p, List<Integer> keys)
    {
        if(p == null)
            return;

        KeyData.put(p, keys);
    }

    public static boolean isKeyPress(EntityPlayer p, int key)
    {
        if(p == null)
            return false;

        if(KeyData.containsKey(p))
            if(KeyData.get(p).contains(key))
                return true;

        return false;
    }

    public static void setPlayerMode(EntityPlayer p, String key)
    {
        if(p == null)
            return;

        PlayerMode.put(p, key);
    }

    public static boolean hasPlayerMode(EntityPlayer p)
    {
        if(p == null)
            return false;

        return PlayerMode.containsKey(p);
    }

    public static boolean PlayerMode(EntityPlayer p, String key)
    {
        if(p == null)
            return false;

        if(PlayerMode.containsKey(p))
                return PlayerMode.get(p).equals(key);

        return false;
    }
}
