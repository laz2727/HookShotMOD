package ssHookShot.client.render;

import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import ssHookShot.Entity.EntityKenn;
import ssHookShot.client.ClientProxy;

@SideOnly(Side.CLIENT)
public class RenderHa extends Render
{
    public void doRender(EntityKenn e, double par2, double par4, double par6, float par8, float par9)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef((float)par2, (float)par4, (float)par6);
        GL11.glRotatef(e.prevRotationYaw + (e.rotationYaw - e.prevRotationYaw) * par9 - 90.0F, 0.0F, 1.0F, 0.0F);
        ClientProxy.剣モデル.描画(e.ticksInAir*100);
        GL11.glPopMatrix();
    }

    public void doRender(Entity par1Entity, double par2, double par4, double par6, float par8, float par9)
    {
        this.doRender((EntityKenn)par1Entity, par2, par4, par6, par8, par9);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity entity) {
        return null;
    }
}
