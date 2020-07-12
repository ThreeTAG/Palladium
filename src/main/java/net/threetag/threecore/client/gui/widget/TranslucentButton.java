package net.threetag.threecore.client.gui.widget;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.ITextComponent;

public class TranslucentButton extends Button {

    // Credit: Vazkii

    public TranslucentButton(int xPos, int yPos, int width, int height, ITextComponent displayString, IPressable handler) {
        super(xPos, yPos, width, height, displayString, handler);
    }

    @Override
    public void func_238474_b_(MatrixStack stack, int x, int y, int textureU, int textureV, int width, int height) {
        AbstractGui.func_238467_a_(stack, x, y, x + width, y + height, Integer.MIN_VALUE);
    }

}
