package net.threetag.threecore.util.client.gui;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.widget.button.Button;

public class TranslucentButton extends Button {

    // Credit: Vazkii

    public TranslucentButton(int xPos, int yPos, int width, int height, String displayString, IPressable handler) {
        super(xPos, yPos, width, height, displayString, handler);
    }

    @Override
    public void blit(int x, int y, int textureU, int textureV, int width, int height) {
        AbstractGui.fill(x, y, x + width, y + height, Integer.MIN_VALUE);
    }

}
