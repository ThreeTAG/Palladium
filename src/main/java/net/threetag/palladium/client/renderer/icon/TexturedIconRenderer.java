package net.threetag.palladium.client.renderer.icon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
import net.threetag.palladium.icon.TexturedIcon;
import net.threetag.palladium.logic.context.DataContext;

public class TexturedIconRenderer implements IconRenderer<TexturedIcon> {

    @Override
    public void draw(TexturedIcon icon, Minecraft mc, GuiGraphics guiGraphics, DataContext context, int x, int y, int width, int height) {
        var texture = icon.texture().withPath(context, "textures/icon/", ".png");

        var r = icon.tint().getRed();
        var g = icon.tint().getGreen();
        var b = icon.tint().getBlue();
        var a = icon.tint().getAlpha();

        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(x, y);
        guiGraphics.pose().scale(width / 16F, height / 16F);
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, 0, 0, 0, 0, 16, 16, 16, 16, ARGB.color(a, r, g, b));
        guiGraphics.pose().popMatrix();
    }
}
