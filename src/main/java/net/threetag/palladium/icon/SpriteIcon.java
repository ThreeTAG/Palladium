package net.threetag.palladium.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record SpriteIcon(Identifier sprite) implements Icon {

    public static final MapCodec<SpriteIcon> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance
            .group(
                    Identifier.CODEC.fieldOf("sprite").forGetter(SpriteIcon::sprite)
            ).apply(instance, SpriteIcon::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, SpriteIcon> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC, icon -> icon.sprite,
            SpriteIcon::new
    );

    @Override
    public IconSerializer<SpriteIcon> getSerializer() {
        return IconSerializers.SPRITE.get();
    }

    @Override
    public @NonNull String toString() {
        return "SpriteIcon{" +
                "sprite=" + sprite +
                '}';
    }

    public static class Serializer extends IconSerializer<SpriteIcon> {

        @Override
        public MapCodec<SpriteIcon> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, SpriteIcon> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
