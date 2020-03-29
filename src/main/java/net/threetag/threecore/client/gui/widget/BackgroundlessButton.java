package net.threetag.threecore.client.gui.widget;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;

public class BackgroundlessButton extends Button {


    public BackgroundlessButton(int widthIn, int heightIn, int posX, int posY, String text, IPressable onPress) {
        super(widthIn, heightIn, posX, posY, text, onPress);
    }

    @Override
    public void blit(int x, int y, int textureU, int textureV, int width, int height) {

    }

    @Override
    public void drawCenteredString(FontRenderer fontRenderer, String text, int x, int y, int color) {
        fontRenderer.drawString(text, (float) (x - fontRenderer.getStringWidth(text) / 2), (float) y, color);
    }
}
