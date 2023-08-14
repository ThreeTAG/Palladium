package net.threetag.palladium.util.icon;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.threetag.palladium.util.context.DataContext;

public interface IIcon {

    @Environment(EnvType.CLIENT)
    default void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y) {
        this.draw(mc, guiGraphics, context, x, y, 16, 16);
    }

    @Environment(EnvType.CLIENT)
    void draw(Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height);

    IconSerializer<?> getSerializer();

}
