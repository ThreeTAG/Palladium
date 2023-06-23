package net.threetag.palladium.util.icon;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public interface IIcon {

    @Environment(EnvType.CLIENT)
    default void draw(Minecraft mc, GuiGraphics guiGraphics, int x, int y) {
        this.draw(mc, guiGraphics, x, y, 16, 16);
    }

    @Environment(EnvType.CLIENT)
    void draw(Minecraft mc, GuiGraphics guiGraphics, int x, int y, int width, int height);

    IconSerializer<?> getSerializer();

}
