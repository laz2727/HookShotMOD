package ssHookShot.client.gui;

import cpw.mods.fml.client.FMLClientHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import ssHookShot.inventory.ContainerSouti;

public class GuiMoveLeggings extends GuiContainer {

    private static Minecraft mc = FMLClientHandler.instance().getClient();

    private ResourceLocation 画像 = new ResourceLocation("sshookshot","textures/gui/soutigui.png");

    public GuiMoveLeggings(EntityPlayer p){
        super(new ContainerSouti(p));
        this.ySize = 166;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float f, int i, int j) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture(画像);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
    }
}
