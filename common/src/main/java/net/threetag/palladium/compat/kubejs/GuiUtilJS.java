package net.threetag.palladium.compat.kubejs;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GuiUtilJS {

    public static void drawString(GuiGraphics guiGraphics, Component component, int x, int y, int color) {
        guiGraphics.drawString(Minecraft.getInstance().font, component, x, y, color, false);
    }

    public static void blit(ResourceLocation texture, GuiGraphics guiGraphics, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(texture, x, y, uOffset, vOffset, uWidth, vHeight);
    }

    public static void blit(ResourceLocation texture, GuiGraphics guiGraphics, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight, int textureWidth, int textureHeight) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        guiGraphics.blit(texture, x, y, uOffset, vOffset, uWidth, vHeight, textureWidth, textureHeight);
    }

}
