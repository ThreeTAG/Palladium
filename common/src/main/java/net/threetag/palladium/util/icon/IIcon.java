package net.threetag.palladium.util.icon;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

public interface IIcon {

    @Environment(EnvType.CLIENT)
    default void draw(Minecraft mc, PoseStack stack, int x, int y) {
        this.draw(mc, stack, x, y, 16, 16);
    }

    @Environment(EnvType.CLIENT)
    void draw(Minecraft mc, PoseStack stack, int x, int y, int width, int height);

    IconSerializer<?> getSerializer();

}
