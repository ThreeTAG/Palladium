package net.threetag.palladium.compat.kubejs;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class GuiUtilJS {

    public static void drawString(PoseStack poseStack, Component component, float x, float y, int color) {
        Minecraft.getInstance().font.draw(poseStack, component, x, y, color);
    }

    public static void blit(ResourceLocation texture, Gui gui, PoseStack poseStack, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, texture);
        gui.blit(poseStack, x, y, uOffset, vOffset, uWidth, vHeight);
    }

}
