package ssHookShot.client.model;

import java.lang.reflect.Field;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

public class ModelKenn extends ModelBase{

    private ResourceLocation tex = new ResourceLocation("sshookshot","textures/model/kenn.png");
    private ResourceLocation tex2 = new ResourceLocation("sshookshot","textures/model/kenn2.png");

    private static Minecraft mc = FMLClientHandler.instance().getClient();

    public ModelRenderer blade;
    public ModelRenderer rod;

    public ModelKenn()
    {
        blade = new ModelRenderer(this, 0, 0);
        blade.addBox(0F, -25F, 0F, 1, 45, 3);
        blade.setRotationPoint(0F, 0F, 0F);
        blade.setTextureSize(64, 64);
        blade.mirror = true;
        rod = new ModelRenderer(this, 10, 0);
        rod.addBox(0F, 19F, -1F, 1, 7, 5);
        rod.setRotationPoint(0F, 0F, 0F);
        rod.setTextureSize(64, 64);
        rod.mirror = true;
    }

    public void render(float rot)
    {
        GL11.glPushMatrix();
        mc.renderEngine.bindTexture(tex);
        GL11.glRotatef(rot, 0.0F, 0.0F, 1.0F);
        blade.render(0.035F);
        GL11.glPopMatrix();
    }

    public void render2(boolean hasCrash)
    {
        GL11.glPushMatrix();
        mc.renderEngine.bindTexture(tex);
        GL11.glRotatef(50.0F, 0.0F, 0.0F, 1.0F);//これくらいでバニラの剣と同じ角度
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F,-0.45F,-0.8F);
        if(hasCrash)
            blade.render(0.035F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef(50.0F, 0.0F, 0.0F, 1.0F);//これくらいでバニラの剣と同じ角度
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F,-0.7F,-0.8F);
        rod.render(0.05F);
        GL11.glPopMatrix();
    }

    public void render(Entity e, boolean hasCrash)
    {
        if(hasCrash&&(mc.gameSettings.thirdPersonView != 0||e != mc.thePlayer))
        {
            renderBlade();
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        Render render = RenderManager.instance.getEntityRenderObject(e);
        ModelBiped model = null;

        Class<RenderPlayer> c = RenderPlayer.class;
        Field f = null;
        try {
            f = c.getDeclaredField("field_77109_a");
        } catch (Exception e1) {
            try {
                f = c.getDeclaredField("modelBipedMain");
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        f.setAccessible(true);
        try {
            model = (ModelBiped)f.get(render);
        } catch (Exception e1) {
        }

        GL11.glPushMatrix();

        model.bipedLeftArm.postRender(0.0625F);
        GL11.glTranslatef(0.08F, -0.25F, -0.08F);
        GL11.glRotatef(90.0f, 0, 1, 0);
        GL11.glRotatef(180.0f, 1, 0, 0);
        GL11.glRotatef(-120.0f, 0, 0, 1);
        if(hasCrash&&(mc.gameSettings.thirdPersonView != 0||e != mc.thePlayer))
        {
            renderBlade();
        }
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glPushMatrix();
        GL11.glPushMatrix();

        model.bipedRightArm.postRender(0.07F);
    }

    private void renderBlade()
    {
        mc.renderEngine.bindTexture(tex2);
        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(-0.6D, 0.0D, 0.0D, (double)0, (double)0);
        tessellator.addVertexWithUV(1.0D, 0.0D, 0.0D, (double)0, (double)1);
        tessellator.addVertexWithUV(1.0D, 1.6D, 0.0D, (double)1, (double)1);
        tessellator.addVertexWithUV(-0.6D, 1.6D, 0.0D, (double)1, (double)0);
        tessellator.draw();
    }
}
