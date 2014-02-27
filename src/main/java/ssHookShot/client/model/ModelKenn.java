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

    private ResourceLocation 画像 = new ResourceLocation("sshookshot","textures/model/kenn.png");
    private ResourceLocation 画像2 = new ResourceLocation("sshookshot","textures/model/kenn2.png");

    private static Minecraft mc = FMLClientHandler.instance().getClient();

    public ModelRenderer 剣;
    public ModelRenderer 柄;

    public ModelKenn()
    {
        剣 = new ModelRenderer(this, 0, 0);
        剣.addBox(0F, -25F, 0F, 1, 45, 3);
        剣.setRotationPoint(0F, 0F, 0F);
        剣.setTextureSize(64, 64);
        剣.mirror = true;
        柄 = new ModelRenderer(this, 10, 0);
        柄.addBox(0F, 19F, -1F, 1, 7, 5);
        柄.setRotationPoint(0F, 0F, 0F);
        柄.setTextureSize(64, 64);
        柄.mirror = true;
    }

    public void 描画(float 角度)
    {
        GL11.glPushMatrix();
        mc.renderEngine.bindTexture(画像);
        GL11.glRotatef(角度, 0.0F, 0.0F, 1.0F);
        剣.render(0.035F);
        GL11.glPopMatrix();
    }

    public void 描画2(Entity e,boolean 折れてるか)
    {
        GL11.glPushMatrix();
        mc.renderEngine.bindTexture(画像);
        GL11.glRotatef(50.0F, 0.0F, 0.0F, 1.0F);//これくらいでバニラの剣と同じ角度
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F,-0.45F,-0.8F);
        if(折れてるか)
            剣.render(0.035F);
        GL11.glPopMatrix();
        GL11.glPushMatrix();
        GL11.glRotatef(50.0F, 0.0F, 0.0F, 1.0F);//これくらいでバニラの剣と同じ角度
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F,-0.7F,-0.8F);
        柄.render(0.05F);
        GL11.glPopMatrix();
    }

    public void 描画(Entity e,boolean 折れてるか)
    {
        if(折れてるか&&(mc.gameSettings.thirdPersonView != 0||e != mc.thePlayer))
        {
            剣の描画();
        }
        GL11.glPopMatrix();
        GL11.glPopMatrix();
        GL11.glPopMatrix();

        Render render = RenderManager.instance.getEntityRenderObject(e);
        RenderPlayer renderplayer = (RenderPlayer)render;
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
        GL11.glRotatef(90.0f,0,1,0);
        GL11.glRotatef(180.0f,1,0,0);
        GL11.glRotatef(-120.0f,0,0,1);
        if(折れてるか&&(mc.gameSettings.thirdPersonView != 0||e != mc.thePlayer))
        {
            剣の描画();
        }
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glPushMatrix();
        GL11.glPushMatrix();

        model.bipedRightArm.postRender(0.07F);
    }

    private void 剣の描画()
    {
        mc.renderEngine.bindTexture(画像2);
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
