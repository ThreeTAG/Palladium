package com.threetag.threecore.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.item.crafting.IRecipe;

public interface IIcon {

    default void draw(Minecraft mc) {
        draw(mc, 0, 0);
    }

    void draw(Minecraft mc, int x, int y);

    int getWidth();

    int getHeight();

    IIconSerializer<?> getSerializer();

}
