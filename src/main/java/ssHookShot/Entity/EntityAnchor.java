package ssHookShot.Entity;

import java.util.List;

import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.IThrowableEntity;
import cpw.mods.fml.relauncher.Side;
import ssHookShot.HookShot;
import ssHookShot.Packet.AnchorSPacket;
import ssHookShot.item.ItemMoveLeggings;
import ssHookShot.system.DataManager;

import javax.xml.crypto.Data;

public class EntityAnchor extends EntityArrow implements IProjectile,IThrowableEntity
{
    public boolean firstUpdate = true;
    private int サイド;//0なら左で1なら右
    private int xTile = -1;
    private int yTile = -1;
    private int zTile = -1;
    public boolean isRec = false;
    public Entity hitEntity;
    private Block inTile;
    private int inData;
    public int inObj;//0なら何にも刺さってない1がブロック2がEntity
    public EntityPlayer shooter;//shooter
    public int ticksInAir;
    public double dist;

    public EntityAnchor(World par1World)
    {
        super(par1World);
        this.ignoreFrustumCheck = true;
        this.setSize(0.5F, 0.5F);
    }

    public EntityAnchor(int サイド,EntityPlayer par2EntityLiving, float par3)
    {
        this(サイド, par2EntityLiving, par3, 0.0F);
    }

    public EntityAnchor(int サイド,EntityPlayer par2EntityLiving, float par3,float yaw)
    {
        super(par2EntityLiving.worldObj);
        this.サイド = サイド;
        this.shooter = par2EntityLiving;
        this.setLocationAndAngles(par2EntityLiving.posX, par2EntityLiving.posY + (double)par2EntityLiving.getEyeHeight(), par2EntityLiving.posZ, par2EntityLiving.rotationYaw + yaw, par2EntityLiving.rotationPitch);
        this.posX -= (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.posY -= 0.1D;
        this.posZ -= (double)(MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * 0.16F);
        this.setPosition(this.posX, this.posY, this.posZ);
        this.yOffset = 0.0F;
        this.motionX = (double)(-MathHelper.sin(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionZ = (double)(MathHelper.cos(this.rotationYaw / 180.0F * (float)Math.PI) * MathHelper.cos(this.rotationPitch / 180.0F * (float)Math.PI));
        this.motionY = (double)(-MathHelper.sin(this.rotationPitch / 180.0F * (float)Math.PI));
        this.setThrowableHeading(this.motionX, this.motionY, this.motionZ, par3, 1.0F);
    }

    protected void entityInit()
    {
        this.dataWatcher.addObject(17, "");//打ったプレイヤーの名前
        this.dataWatcher.addObject(18, 0);
    }

    public void setThrowableHeading(double par1, double par3, double par5, float par7, float par8)
    {
        float f2 = MathHelper.sqrt_double(par1 * par1 + par3 * par3 + par5 * par5);
        par1 /= (double)f2;
        par3 /= (double)f2;
        par5 /= (double)f2;
        par1 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)par8;
        par3 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)par8;
        par5 += this.rand.nextGaussian() * (double)(this.rand.nextBoolean() ? -1 : 1) * 0.007499999832361937D * (double)par8;
        par1 *= (double)par7;
        par3 *= (double)par7;
        par5 *= (double)par7;
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;
        float f3 = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
        this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
        this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)f3) * 180.0D / Math.PI);
    }

    @SideOnly(Side.CLIENT)
    public void setPositionAndRotation2(double par1, double par3, double par5, float par7, float par8, int par9)
    {
        this.setPosition(par1, par3, par5);
        this.setRotation(par7, par8);
    }

    @SideOnly(Side.CLIENT)
    public void setVelocity(double par1, double par3, double par5)
    {
        this.motionX = par1;
        this.motionY = par3;
        this.motionZ = par5;

        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double(par1 * par1 + par5 * par5);
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(par1, par5) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(par3, (double)f) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch;
            this.prevRotationYaw = this.rotationYaw;
            this.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
        }
    }

    public void onUpdate()
    {
        super.onEntityUpdate();

        if (this.shooter == null) {
            this.setDead();
            return;
        }

        if (this.shooter.isDead) {
            if (!shooter.worldObj.isRemote) {
                if (getSide() == DataManager.right) {
                    ItemMoveLeggings.rightAnchorMap.remove(shooter);
                    HookShot.packetPipeline.sendTo(new AnchorSPacket(-1, DataManager.right), (EntityPlayerMP) shooter);
                } else if (getSide() == DataManager.left) {
                    ItemMoveLeggings.leftAnchorMap.remove(shooter);
                    HookShot.packetPipeline.sendTo(new AnchorSPacket(-1, DataManager.left), (EntityPlayerMP) shooter);
                }
            }
            this.setDead();
            return;
        }

        if (this.shooter == hitEntity) {
            if (!shooter.worldObj.isRemote) {
                if (getSide() == 0) {
                    ItemMoveLeggings.rightAnchorMap.remove(shooter);
                    HookShot.packetPipeline.sendTo(new AnchorSPacket(-1, 0), (EntityPlayerMP) shooter);
                } else if (getSide() == 1) {
                    ItemMoveLeggings.leftAnchorMap.remove(shooter);
                    HookShot.packetPipeline.sendTo(new AnchorSPacket(-1, 0), (EntityPlayerMP) shooter);
                }
            }
            this.setDead();
            return;
        }

        if (this.shooter.getDistanceToEntity(this) > 150) {
            if (!shooter.worldObj.isRemote) {
                if (getSide() == 0) {
                    ItemMoveLeggings.rightAnchorMap.remove(shooter);
                    HookShot.packetPipeline.sendTo(new AnchorSPacket(-1, 0), (EntityPlayerMP) shooter);
                } else if (getSide() == 1) {
                    ItemMoveLeggings.leftAnchorMap.remove(shooter);
                    HookShot.packetPipeline.sendTo(new AnchorSPacket(-1, 0), (EntityPlayerMP) shooter);
                }
            }
            rec();
        }

        if(this.shooter.getCurrentArmor(1) == null ||
                (this.shooter.getCurrentArmor(1) != null &&
                        this.shooter.getCurrentArmor(1).getItem() != HookShot.instance.itemMoveLeg))
        {
            this.setDead();
            return;
        }

        if(firstUpdate && shooter instanceof EntityPlayerMP)//最初の一回だけ呼ばれる
        {
            this.playSound("random.click", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));

            this.dataWatcher.updateObject(17, this.shooter.getDisplayName());
            this.dataWatcher.updateObject(18,this.サイド);
            firstUpdate = false;//二回目呼ばれないように
        }



        if (this.prevRotationPitch == 0.0F && this.prevRotationYaw == 0.0F)
        {
            float f = MathHelper.sqrt_double((this.motionX * this.motionX) + (this.motionZ * this.motionZ));
            this.prevRotationYaw = this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);
            this.prevRotationPitch = this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f) * 180.0D / Math.PI);
        }

        if(isRec){
            Vec3 fc = Vec3.createVectorHelper(this.shooter.posX-this.posX,this.shooter.posY-this.posY,this.shooter.posZ-this.posZ);

            this.motionX = fc.xCoord*0.3F;
            this.motionY = fc.yCoord*0.3F;
            this.motionZ = fc.zCoord*0.3F;

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;

            float f2 = MathHelper.sqrt_double((this.motionX * this.motionX) + (this.motionZ * this.motionZ));
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
            {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F)
            {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
            {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;

            this.setPosition(this.posX, this.posY, this.posZ);

            if(this.getDistanceToEntity(this.shooter)<2)
            {
                this.setDead();
            }

            return;
        }

        Block block = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);

        if (block.getMaterial() != Material.air)
        {
            block.setBlockBoundsBasedOnState(this.worldObj, this.xTile, this.yTile, this.zTile);
            AxisAlignedBB axisalignedbb = block.getCollisionBoundingBoxFromPool(this.worldObj, this.xTile, this.yTile, this.zTile);

            if (axisalignedbb != null && axisalignedbb.isVecInside(this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ)))
            {
                this.inObj = 1;
            }
        }

        if (this.inObj == 1)//ブロックに刺さっているとき
        {
            Block j = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
            int k = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);

            if (this.inTile == null || j != this.inTile || k != this.inData) {
                this.inObj = 0;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInAir = 0;
            }
            //ブロックがなくなったら


        }
        else if(this.inObj == 2)//エンティティに刺さってるとき
        {
            this.posX = this.hitEntity.posX;
            this.posY = this.hitEntity.posY;
            this.posZ = this.hitEntity.posZ;

            this.setPosition(this.posX,this.posY,this.posZ);

            if (this.hitEntity.isDead) {
                this.inObj = 0;
                this.motionX *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionY *= (double)(this.rand.nextFloat() * 0.2F);
                this.motionZ *= (double)(this.rand.nextFloat() * 0.2F);
                this.ticksInAir = 0;
            }
            //エンティティがなくなったら


        }
        else//空中にいるとき
        {
            ++this.ticksInAir;
            Vec3 vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
            Vec3 vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);
            MovingObjectPosition movingobjectposition = this.worldObj.func_147447_a(vec3, vec31, false, true, false);
            vec3 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX, this.posY, this.posZ);
            vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(this.posX + this.motionX, this.posY + this.motionY, this.posZ + this.motionZ);

            if (movingobjectposition != null)
            {
                vec31 = this.worldObj.getWorldVec3Pool().getVecFromPool(movingobjectposition.hitVec.xCoord, movingobjectposition.hitVec.yCoord, movingobjectposition.hitVec.zCoord);
            }

            Entity entity = null;
            List list = this.worldObj.getEntitiesWithinAABBExcludingEntity(this, this.boundingBox.addCoord(this.motionX, this.motionY, this.motionZ).expand(1.0D, 1.0D, 1.0D));
            double d0 = 0.0D;
            int l;
            float f1;

            for (l = 0; l < list.size(); ++l)
            {
                Entity entity1 = (Entity)list.get(l);

                if (entity1.canBeCollidedWith() && (entity1 != this.shooter || this.ticksInAir >= 5))
                {
                    f1 = 0.5F;
                    AxisAlignedBB axisalignedbb1 = entity1.boundingBox.expand((double)f1, (double)f1, (double)f1);
                    MovingObjectPosition movingobjectposition1 = axisalignedbb1.calculateIntercept(vec3, vec31);

                    if (movingobjectposition1 != null)
                    {
                        double d1 = vec3.distanceTo(movingobjectposition1.hitVec);

                        if (d1 < d0 || d0 == 0.0D)
                        {
                            entity = entity1;
                            d0 = d1;
                        }
                    }
                }
            }

            if (entity != null)
            {
                movingobjectposition = new MovingObjectPosition(entity);
            }

            float f2;
            float f3;

            if (movingobjectposition != null)
            {
                dist = shooter.getDistanceToEntity(this);

                if (movingobjectposition.entityHit != null && movingobjectposition.entityHit != this.shooter)
                {
                    this.hitEntity = movingobjectposition.entityHit;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inObj = 2;
                }
                else
                {
                    this.xTile = movingobjectposition.blockX;
                    this.yTile = movingobjectposition.blockY;
                    this.zTile = movingobjectposition.blockZ;
                    this.inTile = this.worldObj.getBlock(this.xTile, this.yTile, this.zTile);
                    this.inData = this.worldObj.getBlockMetadata(this.xTile, this.yTile, this.zTile);
                    this.motionX = (double)((float)(movingobjectposition.hitVec.xCoord - this.posX));
                    this.motionY = (double)((float)(movingobjectposition.hitVec.yCoord - this.posY));
                    this.motionZ = (double)((float)(movingobjectposition.hitVec.zCoord - this.posZ));
                    f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionY * this.motionY + this.motionZ * this.motionZ);
                    this.posX -= this.motionX / (double)f2 * 0.05000000074505806D;
                    this.posY -= this.motionY / (double)f2 * 0.05000000074505806D;
                    this.posZ -= this.motionZ / (double)f2 * 0.05000000074505806D;
                    this.playSound("random.bowhit", 1.0F, 1.2F / (this.rand.nextFloat() * 0.2F + 0.9F));
                    this.inObj = 1;

                    if (this.inTile != null)
                    {
                        this.inTile.onEntityCollidedWithBlock(this.worldObj, this.xTile, this.yTile, this.zTile, this);
                    }
                }
            }

            this.posX += this.motionX;
            this.posY += this.motionY;
            this.posZ += this.motionZ;
            f2 = MathHelper.sqrt_double(this.motionX * this.motionX + this.motionZ * this.motionZ);
            this.rotationYaw = (float)(Math.atan2(this.motionX, this.motionZ) * 180.0D / Math.PI);

            for (this.rotationPitch = (float)(Math.atan2(this.motionY, (double)f2) * 180.0D / Math.PI); this.rotationPitch - this.prevRotationPitch < -180.0F; this.prevRotationPitch -= 360.0F);

            while (this.rotationPitch - this.prevRotationPitch >= 180.0F)
            {
                this.prevRotationPitch += 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw < -180.0F)
            {
                this.prevRotationYaw -= 360.0F;
            }

            while (this.rotationYaw - this.prevRotationYaw >= 180.0F)
            {
                this.prevRotationYaw += 360.0F;
            }

            this.rotationPitch = this.prevRotationPitch + (this.rotationPitch - this.prevRotationPitch) * 0.2F;
            this.rotationYaw = this.prevRotationYaw + (this.rotationYaw - this.prevRotationYaw) * 0.2F;
            float f4 = 0.99F;

            if (this.isInWater())
            {
                for (int j1 = 0; j1 < 4; ++j1)
                {
                    f3 = 0.25F;
                    this.worldObj.spawnParticle("bubble", this.posX - this.motionX * (double)f3, this.posY - this.motionY * (double)f3, this.posZ - this.motionZ * (double)f3, this.motionX, this.motionY, this.motionZ);
                }

                f4 = 0.8F;
            }

            this.motionX *= (double)f4;
            this.motionY *= (double)f4;
            this.motionZ *= (double)f4;
            this.setPosition(this.posX, this.posY, this.posZ);
        }
    }

    public void rec(){//回収
        isRec = true;
        inObj = 0;
    }

    public void writeEntityToNBT(NBTTagCompound par1NBTTagCompound){}

    public void readEntityFromNBT(NBTTagCompound par1NBTTagCompound){}

    public void onCollideWithPlayer(EntityPlayer par1EntityPlayer)
    {

    }

    protected boolean canTriggerWalking()
    {
        return false;
    }

    @SideOnly(Side.CLIENT)
    public float getShadowSize()
    {
        return 0.0F;
    }

    public boolean canAttackWithItem()
    {
        return false;
    }

    @Override
    public Entity getThrower() {
        return this.shooter;
    }

    @Override
    public void setThrower(Entity entity) {
        if(shooter == null)
            this.shooter = (EntityPlayer)entity;
    }

    public String getShooterName() {
        return this.dataWatcher.getWatchableObjectString(17);
    }

    public int getSide() {
        return this.dataWatcher.getWatchableObjectInt(18);
    }

    public void setDead()
    {
        super.setDead();
    }

    public boolean isInRangeToRenderDist(double par1)//描画が遠距離でも行われるように
    {
        return true;
    }
}
