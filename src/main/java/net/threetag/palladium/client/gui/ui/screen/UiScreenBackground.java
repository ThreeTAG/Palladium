package net.threetag.palladium.client.gui.ui.screen;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.Palladium;

import java.util.function.Function;

public record UiScreenBackground(boolean sprite, Identifier resource) {

    public static final UiScreenBackground DEFAULT = new UiScreenBackground(Palladium.id("screen/background"));
    public static final UiScreenBackground NONE = new UiScreenBackground();

    public static final Codec<UiScreenBackground> CODEC = Codec.either(Codec.BOOL, Identifier.CODEC.xmap(UiScreenBackground::new, UiScreenBackground::resource))
            .xmap(either -> either.map(
                    b -> b ? DEFAULT : NONE,
                    Function.identity()
            ), background -> background.resource == null ? Either.left(false) : Either.right(background));

    public UiScreenBackground() {
        this(false, null);
    }

    public UiScreenBackground(Identifier resource) {
        this(!resource.getPath().endsWith(".png"), resource);
    }

    public void render(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        if (this.sprite()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.resource, x, y, width, height);
        } else {
            if (this.resource != null) {
                guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.resource, x, y, 0, 0, width, height, 256, 256);
            }
        }
    }
}
