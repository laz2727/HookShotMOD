package ssHookShot.client.render;

import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import ssHookShot.Entity.EntityAnchor;

@SideOnly(Side.CLIENT)
public class RenderAnchor extends Render
{
    Minecraft mc = FMLClientHandler.instance().getClient();
    private static final ResourceLocation field_110780_a = new ResourceLocation("textures/entity/arrow.png");

    public void doRenderAnchor(EntityAnchor entityAnchor, double x, double y, double z, float f1)
    {
        this.bindEntityTexture(entityAnchor);
        GL11.glLineWidth(2.0F);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, (float)z);
        GL11.glRotatef(entityAnchor.prevRotationYaw + (entityAnchor.rotationYaw - entityAnchor.prevRotationYaw) * f1 - 90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(entityAnchor.prevRotationPitch + (entityAnchor.rotationPitch - entityAnchor.prevRotationPitch) * f1, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.instance;
        byte b0 = 0;
        float f2 = 0.0F;
        float f3 = 0.5F;
        float f4 = (float)(b0 * 10) / 32.0F;
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

        int side = 0;
        if(entityAnchor.getSide() == 0)side = 0;
        else if(entityAnchor.getSide() == 1)side = -180;

        if(mc.theWorld.getPlayerEntityByName(entityAnchor.getShooterName()) != null)
        {
            EntityPlayer player = mc.theWorld.getPlayerEntityByName(entityAnchor.getShooterName());
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glColor3f(0, 0, 0);
            GL11.glLineWidth(10);
            if (player != null) {
                tessellator.startDrawing(3);

                tessellator.addVertex(x,y,z);
                tessellator.addVertex(
                        player.lastTickPosX + (player.posX - player.lastTickPosX)* f1 - RenderManager.renderPosX + Math.cos(Math.toRadians(side+player.renderYawOffset))*0.3D,
                        player.lastTickPosY + (player.posY - player.lastTickPosY)* f1 - RenderManager.renderPosY - 0.7D,
                        player.lastTickPosZ + (player.posZ - player.lastTickPosZ)* f1 - RenderManager.renderPosZ + Math.sin(Math.toRadians(side+player.renderYawOffset))*0.3D);

                tessellator.draw();
            }
            GL11.glLineWidth(1);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }
    }

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRenderAnchor((EntityAnchor)par1Entity, par2, par4, par6, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return field_110780_a;
    }
}
