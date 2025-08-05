package net.threetag.palladium.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;

public class GuiUtil {

    public static final int FULL_WHITE = ARGB.white(1F);
    public static final int FULL_BLACK = ARGB.color(255, 0, 0, 0);
    public static final int DEFAULT_GRAY = ARGB.opaque(0x404040);

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
        drawStringWithOutline(guiGraphics, text, x, y, textColor, FULL_BLACK);
    }

}
