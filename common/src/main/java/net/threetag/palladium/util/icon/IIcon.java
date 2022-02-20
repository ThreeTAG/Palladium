package net.threetag.palladium.util.icon;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;

public interface IIcon {

    @Environment(EnvType.CLIENT)
    void draw(Minecraft mc, PoseStack stack, int x, int y);

    IconSerializer<?> getSerializer();

}
