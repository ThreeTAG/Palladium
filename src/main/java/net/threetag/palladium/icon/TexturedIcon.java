package net.threetag.palladium.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.client.texture.TextureReference;
import net.threetag.palladium.util.PalladiumCodecs;

import java.awt.*;

public record TexturedIcon(TextureReference texture, Color tint) implements Icon {

    public static final MapCodec<TexturedIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(
                    TextureReference.CODEC.fieldOf("texture").forGetter(TexturedIcon::texture),
                    PalladiumCodecs.COLOR_CODEC.optionalFieldOf("texture", Color.WHITE).forGetter(TexturedIcon::tint)
            )
            .apply(instance, TexturedIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, TexturedIcon> STREAM_CODEC = StreamCodec.composite(
            TextureReference.STREAM_CODEC, icon -> icon.texture,
            PalladiumCodecs.COLOR_STREAM_CODEC, icon -> icon.tint,
            TexturedIcon::new
    );

    public TexturedIcon(TextureReference texture) {
        this(texture, Color.WHITE);
    }

    public TexturedIcon(Identifier texture) {
        this(texture, Color.WHITE);
    }

    public TexturedIcon(Identifier texture, Color tint) {
        this(TextureReference.normal(texture), tint);
    }

    @Override
    public IconSerializer<TexturedIcon> getSerializer() {
        return IconSerializers.TEXTURE.get();
    }

    @Override
    public String toString() {
        return "TexturedIcon{" +
                "texture=" + texture +
                ", tint=" + tint +
                '}';
    }

    public static class Serializer extends IconSerializer<TexturedIcon> {

        @Override
        public MapCodec<TexturedIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TexturedIcon> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
