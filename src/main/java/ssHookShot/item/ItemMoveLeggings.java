package ssHookShot.item;

import java.util.WeakHashMap;

import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.util.Constants;
import ssHookShot.Entity.EntityAnchor;
import ssHookShot.HookShot;
import ssHookShot.Packet.AnchorPullPacket;
import ssHookShot.client.ClientProxy;
import ssHookShot.system.DataManager;

public class ItemMoveLeggings extends ItemArmor implements ISpecialArmor {
    public WeakHashMap<EntityPlayer, EntityAnchor> 左アンカーマップ = new WeakHashMap<EntityPlayer, EntityAnchor>();
    public WeakHashMap<EntityPlayer, EntityAnchor> 右アンカーマップ = new WeakHashMap<EntityPlayer, EntityAnchor>();

    private ArmorProperties アーマープロパティ = new ArmorProperties(0, 0, 0);

    public ItemMoveLeggings(ArmorMaterial par2EnumArmorMaterial, int par4) {
        super(par2EnumArmorMaterial, 0, par4);
        this.setMaxDamage(-1);//壊れない
    }

    @Override
    public void onArmorTick(World world, EntityPlayer player, ItemStack itemStack) {
        if (!itemStack.hasTagCompound()) {
            itemStack.setTagCompound(new NBTTagCompound());
        }

        if (itemStack.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND) == null) {
            NBTTagList nbttaglist = new NBTTagList();
            itemStack.getTagCompound().setTag("litems", nbttaglist);
        }

        if (itemStack.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND) == null) {
            NBTTagList nbttaglist = new NBTTagList();
            itemStack.getTagCompound().setTag("ritems", nbttaglist);
        }

        if (itemStack.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND) == null) {
            NBTTagList nbttaglist = new NBTTagList();
            itemStack.getTagCompound().setTag("lgbitems", nbttaglist);
        }

        if (itemStack.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND) == null) {
            NBTTagList nbttaglist = new NBTTagList();
            itemStack.getTagCompound().setTag("rgbitems", nbttaglist);
        }

        if (!DataManager.hasPlayerMode(player)) {
            DataManager.setPlayerMode(player, DataManager.modeManual);
        }

        if (!world.isRemote) {
            if (DataManager.isKeyPress(player, DataManager.keyMode)) {
                if (DataManager.PlayerMode(player, DataManager.modeManual)) {
                    DataManager.setPlayerMode(player, DataManager.modeAuto);
                    player.addChatMessage(new ChatComponentText("自動巻き取り有効化"));
                } else if (DataManager.PlayerMode(player, DataManager.modeAuto)) {
                    DataManager.setPlayerMode(player, DataManager.modeManual);
                    player.addChatMessage(new ChatComponentText("距離の維持有効化"));
                }
            }

            double x = 0;
            double y = 0;
            double z = 0;
            double flag = 0;
            double flag2 = 0;

            if (DataManager.isKeyPress(player, DataManager.keyRightAnchorShot)) {
                アンカー発射(0, player, 6.0F, itemStack);
            }

            if (DataManager.isKeyPress(player, DataManager.keyLeftAnchorShot)) {
                アンカー発射(1, player, 6.0F, itemStack);
            }

            if (DataManager.isKeyPress(player, DataManager.keyRightAnchorRec) || DataManager.PlayerMode(player, DataManager.modeAuto)) {
                double[] xyz = アンカー巻き取り(0, player, itemStack);
                x += xyz[0];
                y += xyz[1];
                z += xyz[2];
                flag = xyz[3];
            } else if (DataManager.isKeyPress(player, DataManager.keyRightAnchorExtend)) {
                ワイヤー伸ばす(0, player, itemStack, 0.1D);
                double[] xyz = 距離を保つ(0, player, itemStack);
                x += xyz[0];
                y += xyz[1];
                z += xyz[2];
                flag = xyz[3];
            } else {
                double[] xyz = 距離を保つ(0, player, itemStack);
                x += xyz[0];
                y += xyz[1];
                z += xyz[2];
                flag = xyz[3];
            }

            if (DataManager.isKeyPress(player, DataManager.keyLeftAnchorRec) || DataManager.PlayerMode(player, DataManager.modeAuto)) {
                double[] xyz = アンカー巻き取り(1, player, itemStack);
                x += xyz[0];
                y += xyz[1];
                z += xyz[2];
                flag2 = xyz[3];
            } else if (DataManager.isKeyPress(player, DataManager.keyLeftAnchorExtend)) {
                ワイヤー伸ばす(1, player, itemStack, 0.1D);
                double[] xyz = 距離を保つ(1, player, itemStack);
                x += xyz[0];
                y += xyz[1];
                z += xyz[2];
                flag2 = xyz[3];
            } else {
                double[] xyz = 距離を保つ(1, player, itemStack);
                x += xyz[0];
                y += xyz[1];
                z += xyz[2];
                flag2 = xyz[3];
            }

            if(flag == 2||flag2 == 2)
            {
                x = 0;
                y = 0;
                z = 0;
            }

            if(flag != 0||flag2 != 0)
                HookShot.packetPipeline.sendTo(new AnchorPullPacket(x, y, z), (EntityPlayerMP) player);

            if (DataManager.isKeyPress(player, DataManager.keyAnchorRec)) {
                アンカー回収(player, itemStack);
            }

            if (DataManager.isKeyPress(player, DataManager.keyOpenGUI)) {
                player.openGui(HookShot.instance, DataManager.moveLeggingsGUIID, player.worldObj, 0, 0, 0);
            }
        }
    }

    public void アンカー解除(EntityPlayer プレイヤー, int サイド) {
        if (サイド == 0) {
            this.左アンカーマップ.remove(プレイヤー);
        } else if (サイド == 1) {
            this.右アンカーマップ.remove(プレイヤー);
        }
    }

    public double[] アンカー巻き取り(int サイド, EntityPlayer player, ItemStack is) {
        double[] xyz = new double[4];
        if (get燃料(is) >= 1) {
            Entity anchor = null;

            if (サイド == 0) {
                if (this.左アンカーマップ.containsKey(player)) {
                    anchor = this.左アンカーマップ.get(player);
                }
            } else if (サイド == 1) {
                if (this.右アンカーマップ.containsKey(player)) {
                    anchor = this.右アンカーマップ.get(player);
                }
            }

            if (anchor != null && ((EntityAnchor) anchor).inObj != 0) {
                set燃料(is, 1, player);

                if(anchor.getDistanceToEntity(player) < 2){
                    xyz[3] = 2;
                    return xyz;
                }

                double xx = player.posX - anchor.posX;
                double yy = player.posY - anchor.posY;
                double zz = player.posZ - anchor.posZ;
                double 角度XZ = Math.atan2(xx, zz);
                double 角度Y = Math.atan2(yy, Math.hypot(xx, zz));

                xyz[0] += -Math.sin(角度XZ) * Math.cos(角度Y);
                xyz[2] -= Math.cos(角度XZ) * Math.cos(角度Y);
                xyz[1] -= Math.sin(角度Y)*2;
                xyz[3] = 1;

                player.fallDistance = 0.0F;
                ((EntityAnchor) anchor).dist = anchor.getDistanceToEntity(player);
            }
        }
        return xyz;
    }

    public void アンカー回収(EntityPlayer プレイヤー, ItemStack is) {
        if (this.左アンカーマップ.containsKey(プレイヤー) && !this.左アンカーマップ.get(プレイヤー).isRec()) {
            this.左アンカーマップ.get(プレイヤー).rec();
        }

        if (this.右アンカーマップ.containsKey(プレイヤー) && !this.右アンカーマップ.get(プレイヤー).isRec()) {
            this.右アンカーマップ.get(プレイヤー).rec();
        }
    }

    private void ワイヤー伸ばす(int サイド, EntityPlayer プレイヤー, ItemStack is, double 長さ) {
        Entity 引く方 = null;

        if (サイド == 0) {
            if (this.左アンカーマップ.containsKey(プレイヤー)) {
                引く方 = this.左アンカーマップ.get(プレイヤー);
            }
        } else if (サイド == 1) {
            if (this.右アンカーマップ.containsKey(プレイヤー)) {
                引く方 = this.右アンカーマップ.get(プレイヤー);
            }
        }

        if (引く方 != null) {
            ((EntityAnchor) 引く方).dist += 長さ;
        }
    }

    private double[] 距離を保つ(int side, EntityPlayer player, ItemStack is)
    {
        Entity anchor = null;

        double[] xyz = new double[4];

        if (side == 0)
        {
            if (this.左アンカーマップ.containsKey(player))
            {
                anchor = this.左アンカーマップ.get(player);
            }
        }
        else if (side == 1)
        {
            if (this.右アンカーマップ.containsKey(player))
            {
                anchor = this.右アンカーマップ.get(player);
            }
        }

        if (anchor != null && ((EntityAnchor)anchor).inObj != 0)
        {
            float f = anchor.getDistanceToEntity(player);
            player.fallDistance = 0.0F;

            if (((EntityAnchor)anchor).dist == 0.0D)
            {
                ((EntityAnchor)anchor).dist = f;
            }

            if (f > ((EntityAnchor)anchor).dist&&anchor.posY-player.posY > 1)
            {
                double xx = player.posX - anchor.posX;
                double yy = player.posY - anchor.posY-((EntityAnchor)anchor).dist;
                double zz = player.posZ - anchor.posZ;
                double 角度XZ = Math.atan2(xx, zz);
                double 角度Y = Math.atan2(yy, Math.hypot(xx, zz));

                xyz[0] += -Math.sin(角度XZ) * Math.cos(角度Y);
                xyz[2] -= Math.cos(角度XZ) * Math.cos(角度Y);
                xyz[1] -= Math.sin(角度Y);

                xyz[0] *= 0.4D;
                xyz[1] *= 0.4D;
                xyz[2] *= 0.4D;
                xyz[3] = 1;
            }
        }
        return xyz;
    }


    public void アンカー発射(int サイド, EntityPlayer player, float 速度, ItemStack is) {
        if (get燃料(is) > 4) {
            if (サイド == 0) {
                if (this.左アンカーマップ.containsKey(player) && !this.左アンカーマップ.get(player).isRec()) {
                    this.左アンカーマップ.get(player).rec();
                    return;
                }
            } else if (サイド == 1) {
                if (this.右アンカーマップ.containsKey(player) && !this.右アンカーマップ.get(player).isRec()) {
                    this.右アンカーマップ.get(player).rec();
                    return;
                }
            }

            if (サイド == 0 && !this.左アンカーマップ.containsKey(player)) {
                EntityAnchor anchor = new EntityAnchor(0, player, 速度);
                set燃料(is, 4, player);
                左アンカーマップ.put(player, anchor);
                player.worldObj.spawnEntityInWorld(anchor);
            } else if (サイド == 1 && !this.右アンカーマップ.containsKey(player)) {
                EntityAnchor anchor = new EntityAnchor(1, player, 速度);
                set燃料(is, 4, player);
                右アンカーマップ.put(player, anchor);
                player.worldObj.spawnEntityInWorld(anchor);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        return ClientProxy.moveLegModel;
    }

    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return アーマープロパティ;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {
    }

    public static int get燃料(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND) != null && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0 && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                NBTTagCompound nbt = (NBTTagCompound) is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                int j = nbt.getByte("Slot") & 255;
                ItemStack 左 = ItemStack.loadItemStackFromNBT(nbt);
                nbt = (NBTTagCompound) is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                j = nbt.getByte("Slot") & 255;
                ItemStack 右 = ItemStack.loadItemStackFromNBT(nbt);
                return ((左.getMaxDamage() + 右.getMaxDamage()) - (左.getItemDamage() + 右.getItemDamage()));
            }
        }

        return 0;
    }

    public static int 最大燃料量(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND) != null && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0 && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                NBTTagCompound nbt = (NBTTagCompound) is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                int j = nbt.getByte("Slot") & 255;
                ItemStack 左 = ItemStack.loadItemStackFromNBT(nbt);
                nbt = (NBTTagCompound) is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                j = nbt.getByte("Slot") & 255;
                ItemStack 右 = ItemStack.loadItemStackFromNBT(nbt);
                return 左.getMaxDamage() + 右.getMaxDamage();
            }
        }

        return 0;
    }

    public static boolean 左燃料あるか(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                return true;
            }
        }

        return false;
    }

    public static boolean 右燃料あるか(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                return true;
            }
        }

        return false;
    }

    public static void set燃料(ItemStack is, int ダメージ, EntityPlayer p) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND) != null && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0 && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                int j = nbttagcompound1.getByte("Slot") & 255;
                ItemStack 左 = ItemStack.loadItemStackFromNBT(nbttagcompound1);
                nbttagcompound1 = (NBTTagCompound) is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                j = nbttagcompound1.getByte("Slot") & 255;
                ItemStack 右 = ItemStack.loadItemStackFromNBT(nbttagcompound1);
                左.setItemDamage(左.getItemDamage() + ダメージ);
                右.setItemDamage(右.getItemDamage() + ダメージ);
                NBTTagList nbttaglist = new NBTTagList();
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) 0);
                左.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
                p.getCurrentArmor(1).getTagCompound().setTag("lgbitems", nbttaglist);
                nbttaglist = new NBTTagList();
                nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte) 0);
                右.writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
                p.getCurrentArmor(1).getTagCompound().setTag("rgbitems", nbttaglist);
            }
        }
    }

    public static boolean 刃があるか(ItemStack is) {
        if (is == null || !is.hasTagCompound()) {
            return false;
        } else if (is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND) != null && is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND).tagCount() > 0 && is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                return true;
            }
        }

        return false;
    }

    public static void 右刃を一枚使う(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                if (is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND) != null) {
                    NBTTagList nbttaglist = is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        NBTTagCompound nbt = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
                        int j = nbt.getByte("Slot") & 255;

                        if (j >= 0 && j < 6) {
                            nbttaglist.removeTag(i);
                            return;
                        }
                    }
                }
            }
        }
    }

    public static void 左刃を一枚使う(ItemStack is) {
        if (is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                if (is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND) != null) {
                    NBTTagList nbttaglist = is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.getCompoundTagAt(i);
                        int j = nbttagcompound1.getByte("Slot") & 255;

                        if (j >= 0 && j < 6) {
                            nbttaglist.removeTag(i);
                            return;
                        }
                    }
                }
            }
        }
    }
}
