package net.threetag.palladium.client.texture;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.data.DataContext;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;

public class StaticTexture extends DynamicTexture {

    public static final MapCodec<StaticTexture> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            ResourceLocation.CODEC.fieldOf("texture").forGetter(t -> t.texture)
    ).apply(instance, StaticTexture::new));

    public final ResourceLocation texture;

    public StaticTexture(ResourceLocation texture) {
        this.texture = texture;
    }

    @Override
    public ResourceLocation getTexture(DataContext context) {
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
                    .add("texture", TYPE_RESOURCE_LOCATION, "The texture to use.")
                    .setExampleObject(new StaticTexture(ResourceLocation.fromNamespaceAndPath("example", "textures/example.png")));
        }
    }
}
