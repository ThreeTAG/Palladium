package net.threetag.threecore.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class BackgroundlessButton extends Button {


    public BackgroundlessButton(int widthIn, int heightIn, int posX, int posY, ITextComponent text, IPressable onPress) {
        super(widthIn, heightIn, posX, posY, text, onPress);
    }


    @Override
    public void func_238474_b_(MatrixStack p_238474_1_, int x, int y, int textureU, int textureV, int width, int height) {

    }


    @Override
    public void func_238471_a_(MatrixStack stack, FontRenderer fontRenderer, String text, int x, int y, int color) {
        fontRenderer.func_238421_b_(stack, text, (float) (x - fontRenderer.getStringWidth(text) / 2), (float) y, color);
    }
}
