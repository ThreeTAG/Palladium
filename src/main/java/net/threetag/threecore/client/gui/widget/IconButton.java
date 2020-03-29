package net.threetag.threecore.client.gui.widget;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.icon.IIcon;

public class IconButton extends Button {

    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(ThreeCore.MODID, "textures/gui/widgets.png");

    private final IIcon icon;

    public IconButton(int xPos, int yPos, IIcon icon, IPressable handler) {
        super(xPos, yPos, 20, 20, "", handler);
        this.icon = icon;
    }

    @Override
    public void renderButton(int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered()) * 20;
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        this.blit(this.x, this.y, 0, i, 20, this.height);
        this.icon.draw(minecraft, this.x + 2, this.y + 2);
        this.renderBg(minecraft, mouseX, mouseY);
        RenderSystem.disableBlend();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1F);
    }
}
