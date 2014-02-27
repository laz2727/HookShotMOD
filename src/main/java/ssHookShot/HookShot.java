package ssHookShot;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import ssHookShot.Entity.EntityAnchor;
import ssHookShot.Entity.EntityKenn;
import ssHookShot.item.ItemKenn;
import ssHookShot.item.ItemMoveLeggings;
import ssHookShot.system.CommonProxy;
import ssHookShot.Packet.PacketPipeline;

import java.util.ArrayList;

@Mod(modid="ssHookShot", name="立体なんとか装置みたいなの")
public class HookShot {
    public static final PacketPipeline packetPipeline = new PacketPipeline();

    public static boolean e = false;

    public static ArrayList<EntityPlayer> enablePlayers = new ArrayList<EntityPlayer>();

    public Item moveLeg;
    public Item 燃料;
    public Item 替え刃;
    public Item 折れた刃;
    public Item 折れた刃をまとめたもの;
    public Item 剣;


    @SidedProxy(clientSide = "ssHookShot.client.ClientProxy", serverSide = "ssHookShot.system.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance("ssHookShot")
    public static HookShot instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        proxy.register();
        NetworkRegistry.INSTANCE.registerGuiHandler(this, this.proxy);
        EntityRegistry.registerModEntity(EntityAnchor.class, "entityAnchor", 0, this, 250, 1, true);
        EntityRegistry.registerModEntity(EntityKenn.class, "entityHa", 1, this, 250, 1,true);

        this.moveLeg = new ItemMoveLeggings(ItemArmor.ArmorMaterial.CHAIN,2).setTextureName("sshookshot:moveleg").setUnlocalizedName("hookshot");
        GameRegistry.registerItem(this.moveLeg, "hookshot");

        GameRegistry.addRecipe(
                new ItemStack(moveLeg,1),
                new Object[]{
                        "A A",
                        "BCB",
                        "F F",
                        'C',Item.itemRegistry.getObject("chainmail_leggings"),
                        'F',Item.itemRegistry.getObject("fishing_rod"),
                        'A',Item.itemRegistry.getObject("arrow"),
                        'B',Item.itemRegistry.getObject("bow"),
                });

        this.燃料 = new Item().setTextureName("sshookshot:nennryou").setCreativeTab(CreativeTabs.tabCombat).setMaxStackSize(1).setMaxDamage(24000).setUnlocalizedName("nennryou");
        GameRegistry.registerItem(this.燃料, "nennryou");

        GameRegistry.addRecipe(
                new ItemStack(燃料, 1),
                new Object[]{
                        "M",
                        "S",
                        'M',Item.itemRegistry.getObject("coal"),
                        'S',Item.itemRegistry.getObject("fireworks"),
                });

        this.替え刃 = new Item().setTextureName("sshookshot:kaeba").setCreativeTab(CreativeTabs.tabCombat).setMaxStackSize(1).setUnlocalizedName("kaeba");
        GameRegistry.registerItem(this.替え刃, "kaeba");


        GameRegistry.addRecipe(
                new ItemStack(替え刃, 8),
                new Object[]{
                        " T ",
                        " T ",
                        "S S",
                        'T', Item.itemRegistry.getObject("iron_ingot"),
                        'S', Item.itemRegistry.getObject("stick")
                });

        this.折れた刃 = new Item().setTextureName("sshookshot:oretaha").setCreativeTab(CreativeTabs.tabCombat).setUnlocalizedName("oretaha");
        GameRegistry.registerItem(this.折れた刃, "oretaha");

        this.折れた刃をまとめたもの = new Item().setTextureName("sshookshot:oretahamatometamono").setCreativeTab(CreativeTabs.tabCombat).setUnlocalizedName("oretahamatometamono");
        GameRegistry.registerItem(this.折れた刃をまとめたもの, "oretahamatometamono");

        GameRegistry.addRecipe(
                new ItemStack(折れた刃をまとめたもの, 1),
                new Object[]{
                        "STS",
                        "TTT",
                        "STS",
                        'T',折れた刃,
                        'S',Item.itemRegistry.getObject("stick")
                });

        GameRegistry.addSmelting(
                折れた刃をまとめたもの,
                new ItemStack((Item) Item.itemRegistry.getObject("iron_ingot"), 1),
                1
        );

        this.剣 = new ItemKenn().setUnlocalizedName("kenn");;
        GameRegistry.registerItem(this.剣, "kenn");

        GameRegistry.addRecipe(
                new ItemStack(剣, 1),
                new Object[]{
                        " S ",
                        "BB ",
                        "BB ",
                        'S', Item.itemRegistry.getObject("iron_ingot"),
                        'B', Item.itemRegistry.getObject("stick")
                });
    }

    @Mod.EventHandler
    public void initialise(FMLInitializationEvent evt) {
        packetPipeline.initialise();
    }

    @Mod.EventHandler
    public void postInitialise(FMLPostInitializationEvent evt) {
        packetPipeline.postInitialise();
    }
}
