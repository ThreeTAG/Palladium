package net.threetag.palladium.client.gui.ui.screen;

import com.mojang.serialization.Codec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.Palladium;

public record UiScreenBackground(boolean sprite, Identifier resource) {

    public static final UiScreenBackground DEFAULT = new UiScreenBackground(Palladium.id("screen/background"));
    public static final Codec<UiScreenBackground> CODEC = Identifier.CODEC.xmap(UiScreenBackground::new, UiScreenBackground::resource);

    public UiScreenBackground(Identifier resource) {
        this(!resource.getPath().endsWith(".png"), resource);
    }

    public void render(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        if(this.sprite()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.resource, x, y, width, height);
        } else {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.resource, x, y, 0, 0, width, height, 256, 256);
        }
    }
}
