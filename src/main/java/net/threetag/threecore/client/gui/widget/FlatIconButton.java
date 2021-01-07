package net.threetag.threecore.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.threetag.threecore.ThreeCore;
import net.threetag.threecore.util.icon.IIcon;

public class FlatIconButton extends Button {

    public static final ResourceLocation WIDGETS_LOCATION = new ResourceLocation(ThreeCore.MODID, "textures/gui/widgets.png");

    private final IIcon icon;

    public FlatIconButton(int xPos, int yPos, IIcon icon, IPressable handler) {
        super(xPos, yPos, 20, 20, new StringTextComponent(""), handler);
        this.icon = icon;
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int x, int y, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getTextureManager().bindTexture(WIDGETS_LOCATION);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered()) * 20;
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        this.blit(matrixStack, this.x, this.y, 0, i, 20, this.height);
        this.renderBg(matrixStack, minecraft, x, y);
        this.icon.draw(minecraft, matrixStack, this.x + 2, this.y + 2);

        if (this.isHovered()) {
            this.renderToolTip(matrixStack, x, y);
        }
    }
}
