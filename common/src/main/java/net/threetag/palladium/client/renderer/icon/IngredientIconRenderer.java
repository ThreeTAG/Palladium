package net.threetag.palladium.client.renderer.icon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.threetag.palladium.icon.IngredientIcon;
import net.threetag.palladium.logic.context.DataContext;

public class IngredientIconRenderer implements IconRenderer<IngredientIcon> {

    @SuppressWarnings("deprecation")
    @Override
    public void draw(IngredientIcon icon, Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        var stack = guiGraphics.pose();
        stack.pushMatrix();
        stack.translate(x + width / 2F, y + height / 2F);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            stack.scale(s / 16F, s / 16F);
        }

        var items = icon.ingredient().items().toList();
        int stackIndex = (int) ((System.currentTimeMillis() / 1000) % items.size());
        guiGraphics.renderItem(items.get(stackIndex).value().getDefaultInstance(), -width / 2, -height / 2);
        stack.popMatrix();
    }
}
