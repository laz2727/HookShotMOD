package ssHookShot.client.render;

import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import ssHookShot.Entity.EntityAnchor;
import ssHookShot.client.ClientProxy;

@SideOnly(Side.CLIENT)
public class RenderAnchor extends Render
{
    Minecraft mc = FMLClientHandler.instance().getClient();
    private static final ResourceLocation field_110780_a = new ResourceLocation("textures/entity/arrow.png");

    public void doRenderAnchor(EntityAnchor entityAnchor, double par2, double par4, double par6, float par8, float par9)
    {
        this.bindEntityTexture(entityAnchor);
        GL11.glLineWidth(2.0F);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(entityAnchor.prevRotationYaw + (entityAnchor.rotationYaw - entityAnchor.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entityAnchor.prevRotationPitch + (entityAnchor.rotationPitch - entityAnchor.prevRotationPitch) * par9, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        byte b0 = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float)(0 + b0 * 10) / 32.0F;
        float f5 = (float)(5 + b0 * 10) / 32.0F;
        float f6 = 0.0F;
        float f7 = 0.15625F;
        float f8 = (float)(5 + b0 * 10) / 32.0F;
        float f9 = (float)(10 + b0 * 10) / 32.0F;
        float f10 = 0.05625F;
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);

        GL11.glRotatef(45.0F, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(f10, f10, f10);
        GL11.glTranslatef(-4.0F, 0.0F, 0.0F);
        GL11.glNormal3f(f10, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)f6, (double)f8);
        tessellator.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)f7, (double)f8);
        tessellator.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)f7, (double)f9);
        tessellator.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)f6, (double)f9);
        tessellator.draw();
        GL11.glNormal3f(-f10, 0.0F, 0.0F);
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(-7.0D, 2.0D, -2.0D, (double)f6, (double)f8);
        tessellator.addVertexWithUV(-7.0D, 2.0D, 2.0D, (double)f7, (double)f8);
        tessellator.addVertexWithUV(-7.0D, -2.0D, 2.0D, (double)f7, (double)f9);
        tessellator.addVertexWithUV(-7.0D, -2.0D, -2.0D, (double)f6, (double)f9);
        tessellator.draw();

        for (int i = 0; i < 4; ++i)
        {
            GL11.glRotatef(90.0F, 1.0F, 0.0F, 0.0F);
            GL11.glNormal3f(0.0F, 0.0F, f10);
            tessellator.startDrawingQuads();
            tessellator.addVertexWithUV(-8.0D, -2.0D, 0.0D, (double)f2, (double)f4);
            tessellator.addVertexWithUV(8.0D, -2.0D, 0.0D, (double)f3, (double)f4);
            tessellator.addVertexWithUV(8.0D, 2.0D, 0.0D, (double)f3, (double)f5);
            tessellator.addVertexWithUV(-8.0D, 2.0D, 0.0D, (double)f2, (double)f5);
            tessellator.draw();
        }

        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();

        int サイド = 0;
        if(entityAnchor.getSide() == 0)サイド = 90;
        else if(entityAnchor.getSide() == 1)サイド = -90;

        if (mc.thePlayer != null&&mc.thePlayer.getDisplayName().equals(entityAnchor.getShooterName()))
        {
            float f91 = mc.thePlayer.getSwingProgress(par9);
            float f101 = MathHelper.sin(MathHelper.sqrt_float(f91) * (float) Math.PI);
            Vec3 vec3 = entityAnchor.worldObj.getWorldVec3Pool().getVecFromPool(-0.5D, 0.03D, 0.8D);
            vec3.rotateAroundX(-(mc.thePlayer.prevRotationPitch + (mc.thePlayer.rotationPitch - mc.thePlayer.prevRotationPitch) * par9) * (float)Math.PI / 180.0F);
            vec3.rotateAroundY(-(mc.thePlayer.prevRotationYaw + (mc.thePlayer.rotationYaw - mc.thePlayer.prevRotationYaw) * par9) * (float) Math.PI / 180.0F);
            vec3.rotateAroundY(f101 * 0.5F);
            vec3.rotateAroundX(-f101 * 0.7F);

            double d0 = -Math.cos(((double)mc.thePlayer.rotationYaw+90+サイド) * Math.PI / 180.0D) * 0.4D;
            double d1 = -Math.sin(((double) mc.thePlayer.rotationYaw + 90 + サイド) * Math.PI / 180.0D) * 0.4D;
            double d3 = mc.thePlayer.posX+d0;
            double d4 = mc.thePlayer.posY-0.6F;
            double d5 = mc.thePlayer.posZ+d1;
            double d6 = mc.thePlayer != Minecraft.getMinecraft().thePlayer ? (double)mc.thePlayer.getEyeHeight() : 0.0D;

            if (this.renderManager.options.thirdPersonView > 0 || mc.thePlayer != Minecraft.getMinecraft().thePlayer)
            {
                float f111 = (mc.thePlayer.prevRenderYawOffset + (mc.thePlayer.renderYawOffset - mc.thePlayer.prevRenderYawOffset) * par9) * (float)Math.PI / 180.0F;
                double d7 = (double)MathHelper.sin(f111);
                double d8 = (double)MathHelper.cos(f111);
                d3 = mc.thePlayer.posX+d0;
                d4 = mc.thePlayer.posY-0.6F;
                d5 = mc.thePlayer.posZ+d1;
            }

            double d9 = entityAnchor.prevPosX + (entityAnchor.posX - entityAnchor.prevPosX) * (double)par9;
            double d10 = entityAnchor.prevPosY + (entityAnchor.posY - entityAnchor.prevPosY) * (double)par9 + 0.25D;
            double d11 = entityAnchor.prevPosZ + (entityAnchor.posZ - entityAnchor.prevPosZ) * (double)par9;
            double d12 = (double)((float)(d3 - d9));
            double d13 = (double)((float)(d4 - d10));
            double d14 = (double)((float)(d5 - d11));
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor3f(0, 0, 0);
            tessellator.startDrawing(3);
            tessellator.setColorOpaque_I(0);
            int b2 = 250;

            if(entityAnchor.inObj>0){
                for (int i = 0; i <= b2; ++i)
                {
                    float f12 = (float)i / (float)b2;
                    tessellator.addVertex(par2 + d12 * (double)f12, par4 + d13 * (double)(f12), par6 + d14 * (double)f12);
                }
            }
            else
            {
                for (int i = 0; i <= b2; ++i)
                {
                    float f12 = (float)i / (float)b2;
                    tessellator.addVertex(par2 + d12 * (double)f12, par4 + d13 * -(double)(f12 * f12 + f12) * 0.5D + 0.25D, par6 + d14 * (double)f12);
                }
            }

            tessellator.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
        else if(mc.theWorld.getPlayerEntityByName(entityAnchor.getShooterName()) != null)
        {
            EntityPlayer e = mc.theWorld.getPlayerEntityByName(entityAnchor.getShooterName());
            float f91 = e.getSwingProgress(par9);
            float f101 = MathHelper.sin(MathHelper.sqrt_float(f91) * (float)Math.PI);
            Vec3 vec3 = entityAnchor.worldObj.getWorldVec3Pool().getVecFromPool(-0.5D, 0.03D, 0.8D);
            vec3.rotateAroundX(-(e.prevRotationPitch + (e.rotationPitch - e.prevRotationPitch) * par9) * (float)Math.PI / 180.0F);
            vec3.rotateAroundY(-(e.prevRotationYaw + (e.rotationYaw - e.prevRotationYaw) * par9) * (float)Math.PI / 180.0F);
            vec3.rotateAroundY(f101 * 0.5F);
            vec3.rotateAroundX(-f101 * 0.7F);
            double d0 = -Math.cos(((double)e.rotationYaw+90+サイド) * Math.PI / 180.0D) * 0.4D;
            double d1 = -Math.sin(((double)e.rotationYaw+90+サイド) * Math.PI / 180.0D) * 0.4D;
            double d3 = e.posX+d0;
            double d4 = e.posY-0.6F;
            double d5 = e.posZ+d1;
            double d6 = e != Minecraft.getMinecraft().thePlayer ? (double)e.getEyeHeight() : 0.0D;

            if (this.renderManager.options.thirdPersonView > 0 || e != Minecraft.getMinecraft().thePlayer)
            {
                float f111 = (e.prevRenderYawOffset + (e.renderYawOffset - e.prevRenderYawOffset) * par9) * (float)Math.PI / 180.0F;
                double d7 = (double)MathHelper.sin(f111);
                double d8 = (double)MathHelper.cos(f111);
                d3 = e.posX+d0;
                d4 = e.posY-0.6F;
                d5 = e.posZ+d1;
            }

            double d9 = entityAnchor.prevPosX + (entityAnchor.posX - entityAnchor.prevPosX) * (double)par9;
            double d10 = entityAnchor.prevPosY + (entityAnchor.posY - entityAnchor.prevPosY) * (double)par9 + 0.25D;
            double d11 = entityAnchor.prevPosZ + (entityAnchor.posZ - entityAnchor.prevPosZ) * (double)par9;
            double d12 = (double)((float)(d3 - d9));
            double d13 = (double)((float)(d4 - d10));
            double d14 = (double)((float)(d5 - d11));
            d13+=+1.5F;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor3f(255, 0, 0);
            tessellator.startDrawing(3);
            tessellator.setColorOpaque_I(0);
            int b2 = 250;

            for (int i = 0; i <= b2; ++i)
            {
                float f12 = (float)i / (float)b2;
                tessellator.addVertex(par2 + d12 * (double)f12, par4 + d13 * (double)(f12 * f12 + f12) * 0.75D + 0.25D, par6 + d14 * (double)f12);
            }

            tessellator.draw();
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderAnchor((EntityAnchor)par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return field_110780_a;
    }
}
