package ssHookShot.item;

import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.WeakHashMap;

import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.util.EnumHelper;
import ssHookShot.Entity.EntityKenn;
import ssHookShot.HookShot;
import ssHookShot.client.ClientProxy;
import ssHookShot.system.DataManager;

public class ItemKenn extends ItemSword implements IItemRenderer {
    private static ToolMaterial 剣 = EnumHelper.addToolMaterial("剣", 1, 0, 20, 0, 14);

    @SideOnly(Side.CLIENT)
    private IIcon[] iconArray;

    private final int 耐久値 = 20;

    protected Random rand = new Random();

    public WeakHashMap<EntityPlayer, PlayerXYZ> lastmotion = new WeakHashMap<EntityPlayer, PlayerXYZ>();
    public WeakHashMap<EntityPlayer, Integer> 準備時間 = new WeakHashMap<EntityPlayer, Integer>();

    public ItemKenn() {
        super(剣);
        this.setMaxStackSize(1);
        this.setMaxDamage(耐久値);
        this.setCreativeTab(CreativeTabs.tabCombat);//武器タブ
    }

    public EnumAction getItemUseAction(ItemStack par1ItemStack) {
        return EnumAction.bow;//弓の持ち方
    }

    public int getMaxItemUseDuration(ItemStack par1ItemStack) {
        return 72000;
    }

    public ItemStack onItemRightClick(ItemStack par1ItemStack, World par2World, EntityPlayer par3EntityPlayer) {
        if (準備時間.containsKey(par3EntityPlayer) && 準備時間.get(par3EntityPlayer) == 0) {//準備完了時(準備期間が0の時)のみ剣を構えられる
            par3EntityPlayer.setItemInUse(par1ItemStack, this.getMaxItemUseDuration(par1ItemStack));
        }
        return par1ItemStack;
    }

    public void onUpdate(ItemStack par1ItemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
        if (!par3Entity.worldObj.isRemote) {
            if (par3Entity instanceof EntityPlayerMP) {
                EntityPlayerMP p = (EntityPlayerMP) par3Entity;

                if (!準備時間.containsKey(p))//登録されてなければ
                    準備時間.put(p, 0);//登録
                if (準備時間.get(p) > 0)//準備期間が0以上なら
                    準備時間.put(p, 準備時間.get(p) - 1);//1へらす

                if (par5 && p.isUsingItem()) {
                    List<Entity> list = p.worldObj.getEntitiesWithinAABB(Entity.class, p.boundingBox.expand(1, 1, 1));
                    Iterator<Entity> it = list.iterator();

                    while (it.hasNext()) {
                        Entity e = it.next();
                        if (!(e instanceof EntityXPOrb) && !(e instanceof EntityItem) && e != p)//もっる人と経験値とアイテムには攻撃しない
                        {
                            攻撃(par1ItemStack, p, e, false);
                        }
                    }
                }

                lastmotion.put(p, new PlayerXYZ(p.posX, p.posY, p.posZ));//前tickの座標をほぞん(攻撃力の判定で使う)

                if (DataManager.isKeyPress(p, DataManager.key)&&par1ItemStack.getItemDamage()<this.耐久値) {
                    EntityKenn kenn = new EntityKenn(p, 3.0F, 0);
                    p.worldObj.spawnEntityInWorld(kenn);
                    kenn = new EntityKenn(p, 3.0F, -1);
                    p.worldObj.spawnEntityInWorld(kenn);
                    par1ItemStack.setItemDamage(耐久値 + 1);//剣破壊
                    return;
                } else if (DataManager.isKeyPress(p, DataManager.keyReload) && par1ItemStack.getItemDamage() > 0)//左クリックが押されていて剣が傷ついてたら
                {
                    if (p.getCurrentArmor(1) != null) {
                        if (ItemMoveLeggings.刃があるか(p.getCurrentArmor(1)))//インベントリに替え刃があれば
                        {
                            p.swingItem();//腕を振る
                            ItemMoveLeggings.左刃を一枚使う(p.getCurrentArmor(1));
                            ItemMoveLeggings.右刃を一枚使う(p.getCurrentArmor(1));
                            if (par1ItemStack.getItemDamage() <= 耐久値) {
                                EntityKenn kenn = new EntityKenn(p, 0.0F, 0);
                                p.worldObj.spawnEntityInWorld(kenn);
                                kenn = new EntityKenn(p, 0.0F, -1);
                                p.worldObj.spawnEntityInWorld(kenn);
                            }
                            par1ItemStack.setItemDamage(0);//耐久値回復
                            return;
                        }
                    }

                    boolean flag = false;

                    for (int j = 0; j < p.inventory.mainInventory.length; ++j) {
                        if (p.inventory.mainInventory[j] != null && p.inventory.mainInventory[j].getItem() == HookShot.instance.替え刃) {
                            for (int j1 = j + 1; j1 < p.inventory.mainInventory.length; ++j1) {
                                if (p.inventory.mainInventory[j1] != null && p.inventory.mainInventory[j1].getItem() == HookShot.instance.替え刃) {
                                    flag = true;
                                }
                            }
                        }
                    }

                    if (flag)//インベントリに替え刃があれば
                    {
                        p.swingItem();//腕を振る
                        p.inventory.consumeInventoryItem(HookShot.instance.替え刃);//替え刃を消費する
                        p.inventory.consumeInventoryItem(HookShot.instance.替え刃);//替え刃を消費する
                        if (par1ItemStack.getItemDamage() <= 耐久値) {
                            EntityKenn kenn = new EntityKenn(p, 0.0F, 0);
                            p.worldObj.spawnEntityInWorld(kenn);
                            kenn = new EntityKenn(p, 0.0F, -1);
                            p.worldObj.spawnEntityInWorld(kenn);
                        }
                        par1ItemStack.setItemDamage(0);//耐久値回復
                    }
                }
            }
        }
    }

    public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
        return true;
    }


    public void 攻撃(ItemStack stack, EntityPlayer player, Entity entity, boolean a) {
        if (stack.getItemDamage() < 耐久値 && !player.worldObj.isRemote) {
            double m = 0;
            m += Math.abs(lastmotion.get(player).x - player.posX);
            m += Math.abs(lastmotion.get(player).y - player.posY) * 2;//落下の係数は大きく
            m += Math.abs(lastmotion.get(player).z - player.posZ);
            m *= 10;
            if (m > 0) {
                stack.damageItem((int) m, player);
                entity.attackEntityFrom(DamageSource.causePlayerDamage(player), (int) (m * 2));
                player.stopUsingItem();
                準備時間.put(player, 20);
            } else if (a) {
                stack.damageItem(2, player);//攻撃ではなかったら2ダメージを剣が受ける
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister) {
        this.iconArray = new IIcon[2];
        this.iconArray[0] = par1IconRegister.registerIcon("sshookshot:kennhanasi");//折れてるアイコン
        this.iconArray[1] = par1IconRegister.registerIcon("sshookshot:kennhaari");//折れてないアイコン
    }

    @SideOnly(Side.CLIENT)
    public boolean isFull3D()//立体的に表示する
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    public IIcon getIconFromDamage(int par1)//アイコンをダメージ値によって変更
    {
        if (par1 >= 耐久値)//完全に折れていれば
        {
            return this.iconArray[0];//折れているアイコンを使う
        }
        return this.iconArray[1];
    }

    public boolean onBlockDestroyed(ItemStack par1ItemStack, World par2World, int par3, int par4, int par5, int par6, EntityLivingBase par7EntityLivingBase) {
        //何もしない
        return true;
    }

    private class PlayerXYZ//位置情報保持用の内部クラス
    {
        double x;
        double y;
        double z;

        public PlayerXYZ(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON;//手に持ってるときだけ描画する
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return false;//特に何も使わない
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            if (item.getItemDamage() >= 耐久値)//折れてれば
                ClientProxy.剣モデル.描画2((Entity) data[1], false);//刃を描画しない
            else ClientProxy.剣モデル.描画2((Entity) data[1], true);
        }
        if (item.getItemDamage() >= 耐久値)//折れてれば
            ClientProxy.剣モデル.描画((Entity) data[1], false);//刃を描画しない
        else ClientProxy.剣モデル.描画((Entity) data[1], true);
    }
}
