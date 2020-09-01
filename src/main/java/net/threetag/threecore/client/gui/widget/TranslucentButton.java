package net.threetag.threecore.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class TranslucentButton extends Button {

    // Credit: Vazkii

    public TranslucentButton(int xPos, int yPos, int width, int height, ITextComponent displayString, IPressable handler) {
        super(xPos, yPos, width, height, displayString, handler);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int x, int y, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        FontRenderer fontrenderer = minecraft.fontRenderer;
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, this.alpha);
        int i = this.getYImage(this.isHovered());
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        AbstractGui.fill(matrixStack, this.x, this.y, this.x + this.width, this.y + this.height, Integer.MIN_VALUE);
        if(this.isHovered()) {
            AbstractGui.fill(matrixStack, this.x, this.y, this.x + this.width, this.y + 1, 0xffffffff);
            AbstractGui.fill(matrixStack, this.x, this.y + this.height - 1, this.x + this.width, this.y + this.height, 0xffffffff);
            AbstractGui.fill(matrixStack, this.x, this.y, this.x + 1, this.y + this.height, 0xffffffff);
            AbstractGui.fill(matrixStack, this.x + this.width - 1, this.y, this.x + this.width, this.y + this.height, 0xffffffff);
        }

        this.renderBg(matrixStack, minecraft, x, y);
        int j = getFGColor();
        drawCenteredString(matrixStack, fontrenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, j | MathHelper.ceil(this.alpha * 255.0F) << 24);

        if (this.isHovered()) {
            this.renderToolTip(matrixStack, x, y);
        }
    }

}
