package ssHookShot.client.model;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import ssHookShot.item.ItemMoveLeggings;

public class ModelMoveLeggings extends ModelBiped
{
    private static Minecraft mc = FMLClientHandler.instance().getClient();

    ModelRenderer LParts;
    ModelRenderer RParts;
    ModelRenderer Gus;
    ModelRenderer Gus1;
    ModelRenderer Gus2;

    private ResourceLocation tex = new ResourceLocation("sshookshot","textures/model/moveleg.png");

    public ModelMoveLeggings()
    {
        textureWidth = 64;
        textureHeight = 128;
        setTextureOffset("LParts.LP1", 0, 26);
        setTextureOffset("LParts.LP2", 33, 115);
        setTextureOffset("LParts.LP3", 46, 115);
        setTextureOffset("Gus1.LP4", 0, 70);
        setTextureOffset("LParts.LP5", 0, 115);
        setTextureOffset("LParts.LP6", 0, 115);
        setTextureOffset("LParts.LP7", 13, 115);
        setTextureOffset("LParts.LP8", 13, 115);
        setTextureOffset("LParts.LP9", 13, 115);
        setTextureOffset("LParts.LP10", 13, 115);
        setTextureOffset("LParts.LP11", 22, 115);
        setTextureOffset("LParts.LP12", 22, 115);
        setTextureOffset("LParts.LP13", 22, 115);
        setTextureOffset("RParts.RP1", 0, 0);
        setTextureOffset("RParts.RP2", 33, 102);
        setTextureOffset("RParts.RP3", 46, 102);
        setTextureOffset("Gus2.RP4", 0, 53);
        setTextureOffset("RParts.RP5", 0, 102);
        setTextureOffset("RParts.RP6", 0, 102);
        setTextureOffset("RParts.RP7", 13, 102);
        setTextureOffset("RParts.RP8", 13, 102);
        setTextureOffset("RParts.RP9", 13, 102);
        setTextureOffset("RParts.RP10", 13, 102);
        setTextureOffset("RParts.RP11", 22, 102);
        setTextureOffset("RParts.RP12", 22, 102);
        setTextureOffset("RParts.RP13", 22, 102);
        setTextureOffset("Gus.G1", 0, 90);
        setTextureOffset("Gus.G2", 11, 90);
        setTextureOffset("Gus.G3", 29, 90);
        setTextureOffset("Gus.G4", 40, 90);
        setTextureOffset("Gus.G5", 40, 90);
        setTextureOffset("Gus.G6", 22, 90);

        LParts = new ModelRenderer(this, "LParts");
        LParts.setRotationPoint(3.5F, 15F, 0F);
        setRotation(LParts, -0.3926991F, 0F, 0F);
        LParts.mirror = true;
        LParts.addBox("LP1", 1F, -2.5F, -4F, 4, 8, 17);
        LParts.addBox("LP2", 1.5F, -4F, -5.5F, 4, 1, 2);
        LParts.addBox("LP3", 2F, -3.5F, -4F, 2, 1, 1);
        LParts.addBox("LP5", 0.5F, -5F, -2F, 5, 11, 1);
        LParts.addBox("LP6", 0.5F, -5F, 5F, 5, 11, 1);
        LParts.addBox("LP7", 0.5F, -3F, -6F, 1, 8, 3);
        LParts.addBox("LP8", 2F, -3F, -6F, 1, 8, 3);
        LParts.addBox("LP9", 3.5F, -3F, -6F, 1, 8, 3);
        LParts.addBox("LP10", 5F, -3F, -6F, 1, 8, 3);
        LParts.addBox("LP11", 1F, -3F, -5.5F, 4, 1, 1);
        LParts.addBox("LP12", 1F, 0F, -5.5F, 4, 1, 1);
        LParts.addBox("LP13", 1F, 3.5F, -5.5F, 4, 1, 1);
        RParts = new ModelRenderer(this, "RParts");
        RParts.setRotationPoint(-3.5F, 15F, 1F);
        setRotation(RParts, -0.3926991F, 0F, 0F);
        RParts.mirror = true;
        RParts.addBox("RP1", -5F, -2.5F, -4F, 4, 8, 17);
        RParts.addBox("RP2", -5.5F, -4F, -5.5F, 4, 1, 2);
        RParts.addBox("RP3", -4F, -3.5F, -4F, 2, 1, 1);
        RParts.addBox("RP5", -5.5F, -5F, -2F, 5, 11, 1);
        RParts.addBox("RP6", -5.5F, -5F, 5F, 5, 11, 1);
        RParts.addBox("RP7", -6F, -3F, -6F, 1, 8, 3);
        RParts.addBox("RP8", -4.5F, -3F, -6F, 1, 8, 3);
        RParts.addBox("RP9", -3F, -3F, -6F, 1, 8, 3);
        RParts.addBox("RP10", -1.5F, -3F, -6F, 1, 8, 3);
        RParts.addBox("RP11", -5F, -3F, -5.5F, 4, 1, 1);
        RParts.addBox("RP12", -5F, 0F, -5.5F, 4, 1, 1);
        RParts.addBox("RP13", -5F, 3.5F, -5.5F, 4, 1, 1);

        Gus = new ModelRenderer(this, "Gus");
        Gus.setRotationPoint(0F, 11F, 0F);
        setRotation(Gus, 0F, 0F, 0F);
        Gus.mirror = true;
        Gus.addBox("G1", -2F, -2F, 2F, 4, 4, 1);
        Gus.addBox("G2", -1F, -1F, 3F, 2, 2, 3);
        Gus.addBox("G3", -2F, -0.5F, 4.5F, 4, 1, 1);
        Gus.addBox("G4", -3.5F, -1.5F, 3.5F, 2, 3, 3);
        Gus.addBox("G5", 1.5F, -1.5F, 3.5F, 2, 3, 3);
        Gus.addBox("G6", -0.5F, -1.5F, 3.5F, 1, 3, 2);

        Gus1 = new ModelRenderer(this, "Gus1");
        Gus1.setRotationPoint(3.5F, 15F, 1F);
        setRotation(Gus1, -0.3926991F, 0F, 0F);
        Gus1.mirror = true;
        Gus1.addBox("LP4", 1.5F, -4.5F, -3F, 3, 2, 14);

        Gus2 = new ModelRenderer(this, "Gus2");
        Gus2.setRotationPoint(-3.5F, 15F, 1F);
        setRotation(Gus2, -0.3926991F, 0F, 0F);
        Gus2.mirror = true;
        Gus2.addBox("RP4", -4.5F, -4.5F, -3F, 3, 2, 14);
    }

    public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
    {
        super.setRotationAngles(f, f1, f2, f3, f4, f5,entity);
        if(entity.isSneaking())
        {
            GL11.glTranslatef(0.0F,0.0F,0.4F);
        }

        GL11.glScaled(0.6D,0.6D,0.6D);
        GL11.glTranslatef(0.0F,0.0F,0.1F);
        GL11.glTranslated(0.0D,0.3D,0.0D);
        mc.renderEngine.bindTexture(tex);
        LParts.render(f5);
        RParts.render(f5);
        Gus.render(f5);
        if(ItemMoveLeggings.hasRightFuel(((EntityPlayer) entity).getCurrentArmor(1)))
            Gus1.render(f5);
        if(ItemMoveLeggings.hasLeftFuel(((EntityPlayer) entity).getCurrentArmor(1)))
            Gus2.render(f5);
        GL11.glTranslatef(0.0F,0.0F,-0.1F);
        GL11.glScaled(1.4D,1.4D,1.4D);
        if(entity.isSneaking())
        {
            GL11.glTranslatef(0.0F,0.0F,-0.4F);
        }
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
    {

    }

}
