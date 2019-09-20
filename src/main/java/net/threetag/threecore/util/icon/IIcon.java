package net.threetag.threecore.util.icon;

import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IIcon {

    @OnlyIn(Dist.CLIENT)
    default void draw(Minecraft mc) {
        draw(mc, 0, 0);
    }

    @OnlyIn(Dist.CLIENT)
    void draw(Minecraft mc, int x, int y);

    int getWidth();

    int getHeight();

    IIconSerializer<?> getSerializer();

}
