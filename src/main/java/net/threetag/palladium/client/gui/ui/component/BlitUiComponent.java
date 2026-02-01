package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.client.gui.component.UiAlignment;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

import java.util.Objects;

public record BlitUiComponent(TextureReference texture, UiComponentPosition position, int u, int v, int uOffset, int vOffset, int texWidth,
                              int texHeight) implements RenderableUiComponent {

    public static final MapCodec<BlitUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TextureReference.CODEC.fieldOf("texture").forGetter(BlitUiComponent::texture),
            UiComponentPosition.CODEC.optionalFieldOf("position", UiComponentPosition.TOP_LEFT).forGetter(BlitUiComponent::position),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("u", 0).forGetter(BlitUiComponent::u),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("v", 0).forGetter(BlitUiComponent::v),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("width", 256).forGetter(BlitUiComponent::uOffset),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("height", 256).forGetter(BlitUiComponent::vOffset),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("texture_width", 256).forGetter(BlitUiComponent::texWidth),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("texture_height", 256).forGetter(BlitUiComponent::texHeight)
    ).apply(instance, BlitUiComponent::new));

    public BlitUiComponent(Identifier texture, int x, int y, int u, int v, int uOffset, int vOffset, int texWidth, int texHeight) {
        this(TextureReference.normal(texture), UiComponentPosition.topLeft(x, y), u, v, uOffset, vOffset, texWidth, texHeight);
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.BLIT;
    }

    @Override
    public UiComponentPosition getPosition() {
        return this.position();
    }

    @Override
    public int getWidth() {
        return this.uOffset;
    }

    @Override
    public int getHeight() {
        return this.vOffset;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, UiAlignment alignment) {
        gui.blit(RenderPipelines.GUI_TEXTURED, Objects.requireNonNull(this.texture.getTexture(context)), x, y, this.u, this.v, this.uOffset, this.vOffset, this.texWidth, this.texHeight);
    }

    public static class Serializer extends UiComponentSerializer<BlitUiComponent> {

        @Override
        public MapCodec<BlitUiComponent> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<UiComponent, BlitUiComponent> builder, HolderLookup.Provider provider) {
            builder.setName("Blit")
                    .setDescription("Renders a simple (piece of a) texture")
                    .add("texture", TYPE_TEXTURE_REFERENCE, "Texture path/dynamic texture")
                    .addOptional("position", TYPE_UI_POSITION, "Position of this component", new int[] {0, 0})
                    .addOptional("u", TYPE_NON_NEGATIVE_INT, "U-position on the texture where to start drawing from", 0)
                    .addOptional("v", TYPE_NON_NEGATIVE_INT, "V-position on the texture where to start drawing from", 0)
                    .addOptional("u_offset", TYPE_NON_NEGATIVE_INT, "Width of the piece of the texture that is drawn", 256)
                    .addOptional("v_offset", TYPE_NON_NEGATIVE_INT, "Height of the piece of the texture that is drawn", 256)
                    .addOptional("texture_width", TYPE_NON_NEGATIVE_INT, "Total width of the texture file", 256)
                    .addOptional("texture_height", TYPE_NON_NEGATIVE_INT, "Total height of the texture file", 256);
        }

    }
}
