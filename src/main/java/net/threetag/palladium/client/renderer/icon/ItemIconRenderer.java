package net.threetag.palladium.client.renderer.icon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.threetag.palladium.icon.ItemIcon;
import net.threetag.palladium.logic.context.DataContext;

public class ItemIconRenderer implements IconRenderer<ItemIcon> {

    @Override
    public void draw(ItemIcon icon, Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x + width / 2F, y + height / 2F);

        if (width != 16 || height != 16) {
            int s = Math.min(width, height);
            guiGraphics.pose().scale(s / 16F, s / 16F);
        }

        var item = icon.stack();

        if (item.isEmpty()) {
            var contextItem = context.getItem();

            if (!contextItem.isEmpty()) {
                item = contextItem;
            } else {
                item = new ItemStack(Items.BARRIER);
            }
        }

        guiGraphics.renderItem(item, -width / 2, -height / 2);
        guiGraphics.pose().popMatrix();
    }

}
