package net.threetag.palladium.client.texture.transformer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.Documented;

import java.util.Map;

public abstract class TextureTransformerSerializer<T extends TextureTransformer> implements Documented<TextureTransformer, T> {

    private static final BiMap<ResourceLocation, TextureTransformerSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<TextureTransformerSerializer<?>> TYPE_CODEC = ResourceLocation.CODEC.flatXmap(resourceLocation -> {
        TextureTransformerSerializer<?> serializer = TYPES.get(resourceLocation);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + resourceLocation);
    }, serializer -> {
        ResourceLocation resourceLocation = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(resourceLocation) : DataResult.error(() -> "Unknown type " + resourceLocation);
    });

    public static <T extends TextureTransformer> TextureTransformerSerializer<T> register(ResourceLocation id, TextureTransformerSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for texture transformer serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<ResourceLocation, TextureTransformerSerializer<?>> getTypes() {
        return ImmutableMap.copyOf(TYPES);
    }

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<TextureTransformer, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), TextureTransformer.CODEC, provider);
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<TextureTransformer, T> builder, HolderLookup.Provider provider);

}
