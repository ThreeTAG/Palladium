package net.threetag.palladium.client.renderer.icon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.threetag.palladium.icon.CompoundIcon;
import net.threetag.palladium.icon.Icon;
import net.threetag.palladium.logic.context.DataContext;

public class CompoundIconRenderer implements IconRenderer<CompoundIcon> {

    @Override
    public void draw(CompoundIcon icon, Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        for (Icon i : icon.icons()) {
            IconRenderer.drawIcon(i, mc, guiGraphics, context, x, y, width, height);
        }
    }
}
