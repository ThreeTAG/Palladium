package net.threetag.palladium.client.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class GuiUtil {

    public static void drawStringWithOutline(GuiGraphics guiGraphics, Component text, int x, int y, int textColor, int outlineColor) {
        var font = Minecraft.getInstance().font;

        guiGraphics.drawString(font, text, x + 2, y - 1, outlineColor, false);
        guiGraphics.drawString(font, text, x, y - 1, outlineColor, false);
        guiGraphics.drawString(font, text, x + 1, y, outlineColor, false);
        guiGraphics.drawString(font, text, x + 1, y - 2, outlineColor, false);
        guiGraphics.drawString(font, text, x, y - 2, outlineColor, false);
        guiGraphics.drawString(font, text, x, y, outlineColor, false);
        guiGraphics.drawString(font, text, x + 2, y, outlineColor, false);
        guiGraphics.drawString(font, text, x + 2, y - 2, outlineColor, false);

        guiGraphics.drawString(font, text, x + 1, y - 1, textColor, false);
    }

    public static void drawStringWithBlackOutline(GuiGraphics guiGraphics, Component text, int x, int y, int textColor) {
        drawStringWithOutline(guiGraphics, text, x, y, textColor, RenderUtil.FULL_BLACK);
    }

}
