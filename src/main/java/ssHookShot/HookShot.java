package ssHookShot;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import ssHookShot.Entity.EntityAnchor;
import ssHookShot.Entity.EntityKenn;
import ssHookShot.item.ItemKenn;
import ssHookShot.item.ItemMoveLeggings;
import ssHookShot.system.CommonProxy;
import ssHookShot.Packet.PacketPipeline;

@Mod(modid="ssHookShot", name="立体なんとか装置みたいなの")
public class HookShot {
    public static final PacketPipeline packetPipeline = new PacketPipeline();

    public static boolean e = false;

    public Item itemMoveLeg;
    public Item itemFuel;
    public Item itemBlade;
    public Item itemCrashedBlade;
    public Item itemBladeChank;
    public Item itemSword;

    @SidedProxy(clientSide = "ssHookShot.client.ClientProxy", serverSide = "ssHookShot.system.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance("ssHookShot")
    public static HookShot instance;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, proxy);

        this.itemMoveLeg = new ItemMoveLeggings(ItemArmor.ArmorMaterial.CHAIN,2).setTextureName("sshookshot:moveleg").setUnlocalizedName("hookshot");
        GameRegistry.registerItem(this.itemMoveLeg, "hookshot");

        GameRegistry.addRecipe(
                new ItemStack(itemMoveLeg,1),
                new Object[]{
                        "A A",
                        "BCB",
                        "F F",
                        'C',Item.itemRegistry.getObject("chainmail_leggings"),
                        'F',Item.itemRegistry.getObject("fishing_rod"),
                        'A',Item.itemRegistry.getObject("arrow"),
                        'B',Item.itemRegistry.getObject("bow"),
                });

        this.itemFuel = new Item().setTextureName("sshookshot:nennryou").setCreativeTab(CreativeTabs.tabCombat).setMaxStackSize(1).setMaxDamage(24000).setUnlocalizedName("nennryou");
        GameRegistry.registerItem(this.itemFuel, "nennryou");

        GameRegistry.addRecipe(
                new ItemStack(itemFuel, 1),
                new Object[]{
                        "M",
                        "S",
                        'M',Item.itemRegistry.getObject("coal"),
                        'S',Item.itemRegistry.getObject("fireworks"),
                });

        this.itemBlade = new Item().setTextureName("sshookshot:kaeba").setCreativeTab(CreativeTabs.tabCombat).setMaxStackSize(1).setUnlocalizedName("kaeba");
        GameRegistry.registerItem(this.itemBlade, "kaeba");


        GameRegistry.addRecipe(
                new ItemStack(itemBlade, 8),
                new Object[]{
                        " T ",
                        " T ",
                        "S S",
                        'T', Item.itemRegistry.getObject("iron_ingot"),
                        'S', Item.itemRegistry.getObject("stick")
                });

        this.itemCrashedBlade = new Item().setTextureName("sshookshot:oretaha").setCreativeTab(CreativeTabs.tabCombat).setUnlocalizedName("oretaha");
        GameRegistry.registerItem(this.itemCrashedBlade, "oretaha");

        this.itemBladeChank = new Item().setTextureName("sshookshot:oretahamatometamono").setCreativeTab(CreativeTabs.tabCombat).setUnlocalizedName("oretahamatometamono");
        GameRegistry.registerItem(this.itemBladeChank, "oretahamatometamono");

        GameRegistry.addRecipe(
                new ItemStack(itemBladeChank, 1),
                new Object[]{
                        "STS",
                        "TTT",
                        "STS",
                        'T', itemCrashedBlade,
                        'S',Item.itemRegistry.getObject("stick")
                });

        GameRegistry.addSmelting(
                itemBladeChank,
                new ItemStack((Item) Item.itemRegistry.getObject("iron_ingot"), 1),
                1
        );

        this.itemSword = new ItemKenn().setUnlocalizedName("kenn");;
        GameRegistry.registerItem(this.itemSword, "kenn");

        GameRegistry.addRecipe(
                new ItemStack(itemSword, 1),
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

        EntityRegistry.registerModEntity(EntityAnchor.class, "entityAnchors", 0, this, 250, 1, true);
        EntityRegistry.registerModEntity(EntityKenn.class, "entityHa", 1, this, 250, 1,true);

        proxy.register();

    }

    @Mod.EventHandler
    public void postInitialise(FMLPostInitializationEvent evt) {
        packetPipeline.postInitialise();
    }
}
