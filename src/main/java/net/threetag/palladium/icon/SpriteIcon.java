package net.threetag.palladium.icon;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
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

        @Override
        public void addDocumentation(CodecDocumentationBuilder<Icon, SpriteIcon> builder, HolderLookup.Provider provider) {
            builder.setName("Sprite").setDescription("Renders a sprite as an icon (sprites are located under 'textures/gui/sprites/)")
                    .add("sprite", TYPE_IDENTIFIER, "The ID of the sprite that will be rendered")
                    .addExampleObject(new SpriteIcon(Identifier.fromNamespaceAndPath("example", "my_cool_icon")));
        }
    }
}
