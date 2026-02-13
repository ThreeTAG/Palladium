package net.threetag.palladium.client.gui.ui.component;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.threetag.palladium.client.gui.ui.UiAlignment;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.logic.context.DataContext;

import java.util.Objects;

public final class BlitUiComponent extends RenderableUiComponent {

    public static final MapCodec<BlitUiComponent> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            TextureReference.CODEC.fieldOf("texture").forGetter(BlitUiComponent::texture),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("u", 0).forGetter(BlitUiComponent::u),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("v", 0).forGetter(BlitUiComponent::v),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("texture_width", 256).forGetter(BlitUiComponent::texWidth),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("texture_height", 256).forGetter(BlitUiComponent::texHeight),
            propertiesCodec()
    ).apply(instance, BlitUiComponent::new));

    private final TextureReference texture;
    private final int u;
    private final int v;
    private final int texWidth;
    private final int texHeight;

    public BlitUiComponent(TextureReference texture, int u, int v, int texWidth, int texHeight, UiComponentProperties properties) {
        super(properties);
        this.texture = texture;
        this.u = u;
        this.v = v;
        this.texWidth = texWidth;
        this.texHeight = texHeight;
    }

    public BlitUiComponent(Identifier texture, int u, int v, int texWidth, int texHeight, UiComponentProperties properties) {
        this(TextureReference.normal(texture), u, v, texWidth, texHeight, properties);
    }

    @Override
    public UiComponentSerializer<?> getSerializer() {
        return UiComponentSerializers.BLIT;
    }

    @Override
    public void render(Minecraft minecraft, GuiGraphics gui, DataContext context, int x, int y, int width, int height, int mouseX, int mouseY, UiAlignment alignment) {
        gui.blit(RenderPipelines.GUI_TEXTURED, Objects.requireNonNull(this.texture.getTexture(context)), x, y, this.u, this.v, width, height, this.texWidth, this.texHeight);
    }

    public TextureReference texture() {
        return texture;
    }

    public int u() {
        return u;
    }

    public int v() {
        return v;
    }

    public int texWidth() {
        return texWidth;
    }

    public int texHeight() {
        return texHeight;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (BlitUiComponent) obj;
        return Objects.equals(this.texture, that.texture) &&
                this.u == that.u &&
                this.v == that.v &&
                this.texWidth == that.texWidth &&
                this.texHeight == that.texHeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(texture, u, v, texWidth, texHeight);
    }

    @Override
    public String toString() {
        return "BlitUiComponent[" +
                "texture=" + texture + ", " +
                "u=" + u + ", " +
                "v=" + v + ", " +
                "texWidth=" + texWidth + ", " +
                "texHeight=" + texHeight + ']';
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
                    .addOptional("u", TYPE_NON_NEGATIVE_INT, "U-position on the texture where to start drawing from", 0)
                    .addOptional("v", TYPE_NON_NEGATIVE_INT, "V-position on the texture where to start drawing from", 0)
                    .addOptional("texture_width", TYPE_NON_NEGATIVE_INT, "Total width of the texture file", 256)
                    .addOptional("texture_height", TYPE_NON_NEGATIVE_INT, "Total height of the texture file", 256);
        }

    }
}
