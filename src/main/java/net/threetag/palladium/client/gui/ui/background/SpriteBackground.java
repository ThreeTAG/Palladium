package net.threetag.palladium.client.gui.ui.background;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class SpriteBackground extends UiBackground {

    public static final SpriteBackground DEFAULT = new SpriteBackground(Palladium.id("background/default"));

    public static final MapCodec<SpriteBackground> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("sprite").forGetter(s -> s.sprite)
    ).apply(instance, SpriteBackground::new));

    private final Identifier sprite;

    public SpriteBackground(Identifier sprite) {
        this.sprite = sprite;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.sprite, x, y, width, height);
    }

    @Override
    public UiBackgroundSerializer<?> getSerializer() {
        return UiBackgroundSerializers.SPRITE;
    }

    public static class Serializer extends UiBackgroundSerializer<SpriteBackground> {

        @Override
        public MapCodec<SpriteBackground> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiBackground, SpriteBackground> builder, HolderLookup.Provider provider) {
            builder.setName("Sprite")
                    .setDescription("Renders a sprite accordingly to fill the background.")
                    .add("sprite", TYPE_IDENTIFIER, "Sprite ID.");
        }
    }

}
