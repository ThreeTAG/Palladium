package net.threetag.palladium.client.gui.ui.screen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.Palladium;

public abstract class UiScreenBackground {

    public abstract void render(GuiGraphics guiGraphics, int x, int y, int width, int height);

    public static class Empty extends UiScreenBackground {

        public static final Empty INSTANCE = new Empty();

        @Override
        public void render(GuiGraphics guiGraphics, int x, int y, int width, int height) {
            // nothing
        }
    }

    public static class Simple extends UiScreenBackground {

        public static final Codec<Simple> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("texture").forGetter(s -> s.texture),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("width", 256).forGetter(s -> s.width),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("height", 256).forGetter(s -> s.height)
        ).apply(instance, Simple::new));

        private final Identifier texture;
        private final int width;
        private final int height;

        private Simple(Identifier texture, int width, int height) {
            this.texture = texture;
            this.width = width;
            this.height = height;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int x, int y, int width, int height) {
            guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.texture, x, y, 0, 0, Math.min(width, this.width), Math.min(height, this.height), this.width, this.height);
        }
    }

    public static class RepeatingTexture extends UiScreenBackground {

        public static final RepeatingTexture RED_WOOL = new RepeatingTexture(Identifier.withDefaultNamespace("textures/block/red_wool.png"), 16, 16);

        public static final Codec<RepeatingTexture> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("repeat").forGetter(s -> s.texture),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("width", 16).forGetter(s -> s.width),
                ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("height", 16).forGetter(s -> s.height)
        ).apply(instance, RepeatingTexture::new));

        private final Identifier texture;
        private final int width;
        private final int height;

        private RepeatingTexture(Identifier texture, int width, int height) {
            this.texture = texture;
            this.width = width;
            this.height = height;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int x, int y, int width, int height) {
            guiGraphics.enableScissor(x, y, x + width, y + height);
            for (int i = 0; i < Math.ceil((double) width / this.width); i++) {
                for (int j = 0; j < Math.ceil((double) height / this.height); j++) {
                    guiGraphics.blit(RenderPipelines.GUI_TEXTURED, this.texture,
                            x + (i * this.width), y + (j * this.height),
                            0, 0,
                            this.width, this.height,
                            this.width, this.height);
                }
            }
            guiGraphics.disableScissor();
        }
    }

    public static class Sprite extends UiScreenBackground {

        public static final Sprite DEFAULT = new Sprite(Palladium.id("screen/background"));

        public static final Codec<Sprite> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Identifier.CODEC.fieldOf("sprite").forGetter(s -> s.sprite)
        ).apply(instance, Sprite::new));

        private final Identifier sprite;

        private Sprite(Identifier sprite) {
            this.sprite = sprite;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int x, int y, int width, int height) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, x, y, width, height);
        }
    }
}
