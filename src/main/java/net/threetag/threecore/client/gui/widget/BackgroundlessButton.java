package net.threetag.threecore.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class BackgroundlessButton extends Button {

    // TODO test

    public BackgroundlessButton(int widthIn, int heightIn, int posX, int posY, ITextComponent text, IPressable onPress) {
        super(widthIn, heightIn, posX, posY, text, onPress);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int x, int y, float partialTicks) {
        Minecraft.getInstance().fontRenderer.drawString(matrixStack, this.getMessage().getString(), this.x + (this.width - Minecraft.getInstance().fontRenderer.getStringPropertyWidth(this.getMessage())) / 2F, this.y - 1, 4210752);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, x, y);
        }
    }
}
