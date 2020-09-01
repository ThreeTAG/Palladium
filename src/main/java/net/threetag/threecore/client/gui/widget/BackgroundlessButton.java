package net.threetag.threecore.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

public class BackgroundlessButton extends Button {

    // TODO test

    public BackgroundlessButton(int widthIn, int heightIn, int posX, int posY, ITextComponent text, IPressable onPress) {
        super(widthIn, heightIn, posX, posY, text, onPress);
    }

    @Override
    public void renderButton(MatrixStack matrixStack, int x, int y, float partialTicks) {
        drawCenteredString(matrixStack, Minecraft.getInstance().fontRenderer, this.getMessage(), this.x + this.width / 2, this.y + (this.height - 8) / 2, this.getFGColor() | MathHelper.ceil(this.alpha * 255.0F) << 24);
        if (this.isHovered()) {
            this.renderToolTip(matrixStack, x, y);
        }
    }
}
