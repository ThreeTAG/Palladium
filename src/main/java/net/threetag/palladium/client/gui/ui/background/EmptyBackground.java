package net.threetag.palladium.client.gui.ui.background;

import com.mojang.serialization.MapCodec;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class EmptyBackground extends UiBackground {

    public static final EmptyBackground INSTANCE = new EmptyBackground();
    public static final MapCodec<EmptyBackground> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public void render(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        // nothing
    }

    @Override
    public UiBackgroundSerializer<?> getSerializer() {
        return UiBackgroundSerializers.EMPTY;
    }

    public static class Serializer extends UiBackgroundSerializer<EmptyBackground> {

        @Override
        public MapCodec<EmptyBackground> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiBackground, EmptyBackground> builder, HolderLookup.Provider provider) {
            builder.setName("Empty")
                    .setDescription("Empty background. Renders nothing");
        }
    }
}
