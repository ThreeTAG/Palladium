package net.threetag.palladium.client.texture;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class StaticTexture extends DynamicTexture {

    public static final MapCodec<StaticTexture> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.fieldOf("texture").forGetter(t -> t.texture)
    ).apply(instance, StaticTexture::new));

    public final Identifier texture;

    public StaticTexture(Identifier texture) {
        this.texture = texture;
    }

    @Override
    public Identifier getTexture(DataContext context) {
        return this.texture;
    }

    @Override
    public DynamicTextureSerializer<?> getSerializer() {
        return DynamicTextureSerializers.STATIC;
    }

    public static class Serializer extends DynamicTextureSerializer<StaticTexture> {

        @Override
        public MapCodec<StaticTexture> codec() {
            return CODEC;
        }

        @Override
        public void addDocumentation(CodecDocumentationBuilder<DynamicTexture, StaticTexture> builder, HolderLookup.Provider provider) {
            builder.setName("Static Texture").setDescription("Simple, static texture that does not change.")
                    .add("texture", TYPE_IDENTIFIER, "The texture to use.")
                    .addExampleObject(new StaticTexture(Identifier.fromNamespaceAndPath("example", "textures/example.png")));
        }
    }
}
