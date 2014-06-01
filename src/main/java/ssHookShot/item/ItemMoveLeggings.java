package ssHookShot.item;

import java.util.WeakHashMap;

import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.ISpecialArmor;
import cpw.mods.fml.relauncher.Side;
import net.minecraftforge.common.util.Constants;
import ssHookShot.Entity.EntityAnchor;
import ssHookShot.HookShot;
import ssHookShot.Packet.*;
import ssHookShot.client.ClientProxy;
import ssHookShot.client.MoveHandler;
import ssHookShot.system.DataManager;

public class ItemMoveLeggings extends ItemArmor implements ISpecialArmor {
    public static WeakHashMap<EntityPlayer, EntityAnchor> rightAnchorMap = new WeakHashMap<EntityPlayer, EntityAnchor>();
    public static WeakHashMap<EntityPlayer, EntityAnchor> leftAnchorMap = new WeakHashMap<EntityPlayer, EntityAnchor>();

    private ArmorProperties armorProperty = new ArmorProperties(0, 0, 0);

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

        if (!world.isRemote) {//サーバー側
            if (DataManager.isKeyPress(player, DataManager.keyAnchorRec)) {
                anchorRec(player);
            }

            if (DataManager.isKeyPress(player, DataManager.keyRightAnchorShot)) {
                shotAnchor(DataManager.right, player, 6.0F, itemStack);
            }

            if (DataManager.isKeyPress(player, DataManager.keyLeftAnchorShot)) {
                shotAnchor(DataManager.left, player, 6.0F, itemStack);
            }

            if (DataManager.isKeyPress(player, DataManager.keyOpenGUI)) {
                player.openGui(HookShot.instance, DataManager.moveLeggingsGUIID, player.worldObj, 0, 0, 0);
            }

            if(leftAnchorMap.containsKey(player)&&DataManager.isKeyPress(player,DataManager.keyLeftAnchorRec))
                player.fallDistance = 0;
            else if(rightAnchorMap.containsKey(player)&&DataManager.isKeyPress(player,DataManager.keyRightAnchorRec))
                player.fallDistance = 0;

        } else {//クライアント側
            if (DataManager.isKeyPress(player, DataManager.keyMode)) {
                if (DataManager.PlayerMode(player, DataManager.modeManual)) {
                    DataManager.setPlayerMode(player, DataManager.modeAuto);
                    player.addChatMessage(new ChatComponentTranslation("tile.hookshot.auto", new Object[0]));
                } else if (DataManager.PlayerMode(player, DataManager.modeAuto)) {
                    DataManager.setPlayerMode(player, DataManager.modeManual);
                    player.addChatMessage(new ChatComponentTranslation("tile.hookshot.manual", new Object[0]));
                }
            }

            double[] xyz = anchor(player, itemStack);
            MoveHandler.x = xyz[0];
            MoveHandler.y = xyz[1];
            MoveHandler.z = xyz[2];
            MoveHandler.flag = (int) xyz[3];
        }
    }

    public double[] anchor(EntityPlayer player, ItemStack is) {//クライアント側
        double[] xyz = new double[4];
        if (getFuel(is) >= 1) {
            if (rightAnchorMap.containsKey(player)) {
                EntityAnchor anchor = rightAnchorMap.get(player);
                if (anchor != null && anchor.inObj != 0) {
                    if ((DataManager.isKeyPress(player, DataManager.keyRightAnchorRec) || DataManager.PlayerMode(player, DataManager.modeAuto))) {
                        if (!player.isSneaking()) {
                            if (anchor.getDistanceToEntity(player) > 2) {
                                HookShot.packetPipeline.sendToServer(new FuelPacket(1));
                                double[] a = xyz = anchorPull(anchor, player, xyz);
                                xyz[0] = a[0];
                                xyz[1] = a[1];
                                xyz[2] = a[2];
                                xyz[3] = 1;
                                HookShot.packetPipeline.sendToServer(new DistPacket(0, 0));
                                anchor.dist = anchor.getDistance(player.posX + MoveHandler.mx + a[0],player.posY + MoveHandler.my + a[1], player.posZ + MoveHandler.mz + a[2]);
                            } else xyz[3] = 1;
                        } else if (anchor.inObj == 2) {
                            if (anchor.hitEntity instanceof EntityPlayer) {
                                if (anchor.hitEntity.getDistanceToEntity(player) > 2) {
                                    double ea[] = anchorPull(player, anchor.hitEntity, new double[4]);
                                    HookShot.packetPipeline.sendTo(new AnchorPullPacket(ea[0], ea[1], ea[2], 1), (EntityPlayerMP) anchor.hitEntity);
                                } else
                                    HookShot.packetPipeline.sendTo(new AnchorPullPacket(0.0D, 0.0D, 0.0D, 1), (EntityPlayerMP) anchor.hitEntity);
                            } else {
                                if (anchor.hitEntity.getDistanceToEntity(player) > 2) {
                                    double ea[] = anchorPull(player, anchor.hitEntity, new double[4]);
                                    HookShot.packetPipeline.sendToServer(new EntityPullPacket(anchor.hitEntity.getEntityId(), ea[0], ea[1], ea[2]));
                                } else {
                                    HookShot.packetPipeline.sendToServer(new EntityPullPacket(anchor.hitEntity.getEntityId(), 0, 0, 0));
                                }
                            }
                        }
                    } else if (DataManager.isKeyPress(player, DataManager.keyRightAnchorExtend)) {
                        HookShot.packetPipeline.sendToServer(new DistPacket(0.2D, 0));
                        anchor.dist = anchor.getDistanceToEntity(player)+0.2D;
                    }
                }
            }
            if (leftAnchorMap.containsKey(player)) {
                EntityAnchor anchor = leftAnchorMap.get(player);
                if (anchor != null && anchor.inObj != 0) {
                    if ((DataManager.isKeyPress(player, DataManager.keyLeftAnchorRec) || DataManager.PlayerMode(player, DataManager.modeAuto))) {
                        if (!player.isSneaking()) {
                            if (anchor.getDistanceToEntity(player) > 2) {
                                HookShot.packetPipeline.sendToServer(new FuelPacket(1));
                                double[] a = xyz = anchorPull(anchor, player, xyz);
                                xyz[0] = a[0];
                                xyz[1] = a[1];
                                xyz[2] = a[2];
                                xyz[3] = 1;
                                HookShot.packetPipeline.sendToServer(new DistPacket(0, 1));
                                anchor.dist = anchor.getDistance(player.posX + MoveHandler.mx + a[0],player.posY + MoveHandler.my + a[1], player.posZ + MoveHandler.mz + a[2]);
                            } else xyz[3] = 1;
                        } else if (anchor.inObj == 2) {
                            if (anchor.hitEntity instanceof EntityPlayer) {
                                if (anchor.hitEntity.getDistanceToEntity(player) > 2) {
                                    double ea[] = anchorPull(player, anchor.hitEntity, new double[4]);
                                    HookShot.packetPipeline.sendTo(new AnchorPullPacket(ea[0], ea[1], ea[2], 1), (EntityPlayerMP) anchor.hitEntity);
                                } else
                                    HookShot.packetPipeline.sendTo(new AnchorPullPacket(0.0D, 0.0D, 0.0D, 1), (EntityPlayerMP) anchor.hitEntity);
                            } else {
                                if (anchor.hitEntity.getDistanceToEntity(player) > 2) {
                                    double ea[] = anchorPull(player, anchor.hitEntity, new double[4]);
                                    HookShot.packetPipeline.sendToServer(new EntityPullPacket(anchor.hitEntity.getEntityId(), ea[0], ea[1], ea[2]));
                                } else {
                                    HookShot.packetPipeline.sendToServer(new EntityPullPacket(anchor.hitEntity.getEntityId(), 0, 0, 0));
                                }
                            }
                        }
                    } else if (DataManager.isKeyPress(player, DataManager.keyLeftAnchorExtend)) {
                        HookShot.packetPipeline.sendToServer(new DistPacket(0.2D, 1));
                        anchor.dist = anchor.getDistanceToEntity(player)+0.2D;
                    }
                }
            }
        }
        return xyz;
    }

    private double[] anchorPull(Entity anchor, Entity e, double[] xyz) {//クライアント側
        if (e == null || anchor == null)
            return xyz;

        double xx = anchor.posX - e.posX;
        double yy = anchor.posY - e.posY;
        double zz = anchor.posZ - e.posZ;
        double rotXZ = Math.atan2(xx, zz);
        double rotY = Math.atan2(yy, Math.hypot(xx, zz));

        xyz[0] += Math.sin(rotXZ) * Math.cos(rotY);
        xyz[2] += Math.cos(rotXZ) * Math.cos(rotY);
        xyz[1] += Math.sin(rotY);

        return xyz;
    }

    public void anchorRec(EntityPlayer player) {//サーバー側
        if (rightAnchorMap.containsKey(player) && !rightAnchorMap.get(player).isRec) {
            rightAnchorMap.get(player).rec();
            rightAnchorMap.remove(player);
        }

        if (leftAnchorMap.containsKey(player) && !leftAnchorMap.get(player).isRec) {
            leftAnchorMap.get(player).rec();
            leftAnchorMap.remove(player);
        }
    }

    public void shotAnchor(int サイド, EntityPlayer player, float 速度, ItemStack is) {//サーバー側
        if (サイド == DataManager.right) {
            if (rightAnchorMap.containsKey(player) && !rightAnchorMap.get(player).isRec) {
                rightAnchorMap.get(player).rec();
                rightAnchorMap.remove(player);
                HookShot.packetPipeline.sendTo(new AnchorSPacket(-1, DataManager.right), (EntityPlayerMP) player);
                return;
            }
        } else if (サイド == DataManager.left) {
            if (leftAnchorMap.containsKey(player) && !leftAnchorMap.get(player).isRec) {
                leftAnchorMap.get(player).rec();
                leftAnchorMap.remove(player);
                HookShot.packetPipeline.sendTo(new AnchorSPacket(-1, DataManager.left), (EntityPlayerMP) player);
                return;
            }
        }
        if (getFuel(is) > 4) {
            if (サイド == DataManager.right && !rightAnchorMap.containsKey(player)) {
                EntityAnchor anchor = new EntityAnchor(0, player, 速度);
                rightAnchorMap.put(player, anchor);
                player.worldObj.spawnEntityInWorld(anchor);
                setFuel(is, 4, player);
                HookShot.packetPipeline.sendTo(new AnchorSPacket(anchor.getEntityId(), DataManager.right), (EntityPlayerMP) player);
            } else if (サイド == DataManager.left && !leftAnchorMap.containsKey(player)) {
                EntityAnchor anchor = new EntityAnchor(1, player, 速度);
                leftAnchorMap.put(player, anchor);
                player.worldObj.spawnEntityInWorld(anchor);
                setFuel(is, 4, player);
                HookShot.packetPipeline.sendTo(new AnchorSPacket(anchor.getEntityId(), DataManager.left), (EntityPlayerMP)player);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModelBiped getArmorModel(EntityLivingBase entityLiving, ItemStack itemStack, int armorSlot) {
        return ClientProxy.moveLegModel;
    }

    public ArmorProperties getProperties(EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot) {
        return armorProperty;
    }

    @Override
    public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
        return 0;
    }

    @Override
    public void damageArmor(EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot) {}

    public static int getFuel(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND) != null && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND) != null)
            if (is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0 && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                NBTTagCompound nbt = is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                ItemStack 左 = ItemStack.loadItemStackFromNBT(nbt);
                nbt = is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                ItemStack 右 = ItemStack.loadItemStackFromNBT(nbt);
                return ((左.getMaxDamage() + 右.getMaxDamage()) - (左.getItemDamage() + 右.getItemDamage()));
            }
        return 0;
    }

    public static int getMaxFuel(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND) != null && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND) != null)
            if (is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0 && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                NBTTagCompound nbt = is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                ItemStack 左 = ItemStack.loadItemStackFromNBT(nbt);
                nbt = is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                ItemStack 右 = ItemStack.loadItemStackFromNBT(nbt);
                return 左.getMaxDamage() + 右.getMaxDamage();
            }

        return 0;
    }

    public static boolean hasLeftFuel(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasRightFuel(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                return true;
            }
        }

        return false;
    }

    public static void setFuel(ItemStack is, int ダメージ, EntityPlayer p) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND) != null && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0 && is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                NBTTagCompound nbttagcompound1 = is.getTagCompound().getTagList("lgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
                ItemStack 左 = ItemStack.loadItemStackFromNBT(nbttagcompound1);
                nbttagcompound1 = is.getTagCompound().getTagList("rgbitems", Constants.NBT.TAG_COMPOUND).getCompoundTagAt(0);
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

    public static boolean hasBlade(ItemStack is) {
        if (is == null || !is.hasTagCompound()) {
            return false;
        } else if (is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND) != null && is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND).tagCount() > 0 && is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                return true;
            }
        }

        return false;
    }

    public static void useRightBlade(ItemStack is) {
        if (is.hasTagCompound() && is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                if (is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND) != null) {
                    NBTTagList nbttaglist = is.getTagCompound().getTagList("litems", Constants.NBT.TAG_COMPOUND);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        NBTTagCompound nbt = nbttaglist.getCompoundTagAt(i);
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

    public static void useLeftBlade(ItemStack is) {
        if (is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND) != null) {
            if (is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND).tagCount() > 0) {
                if (is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND) != null) {
                    NBTTagList nbttaglist = is.getTagCompound().getTagList("ritems", Constants.NBT.TAG_COMPOUND);

                    for (int i = 0; i < nbttaglist.tagCount(); ++i) {
                        NBTTagCompound nbttagcompound1 = nbttaglist.getCompoundTagAt(i);
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
