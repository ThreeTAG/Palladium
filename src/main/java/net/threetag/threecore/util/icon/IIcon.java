package net.threetag.threecore.util.icon;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface IIcon {

    @OnlyIn(Dist.CLIENT)
    default void draw(Minecraft mc, MatrixStack stack) {
        draw(mc, stack, 0, 0);
    }

    @OnlyIn(Dist.CLIENT)
    void draw(Minecraft mc, MatrixStack stack, int x, int y);

    int getWidth();

    int getHeight();

    IIconSerializer<?> getSerializer();

}
