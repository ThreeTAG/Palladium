package net.threetag.palladium.client.texture.transformer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.documentation.CodecDocumentationBuilder;
import net.threetag.palladium.documentation.Documented;

import java.util.Map;

public abstract class TextureTransformerSerializer<T extends TextureTransformer> implements Documented<TextureTransformer, T> {

    private static final BiMap<Identifier, TextureTransformerSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<TextureTransformerSerializer<?>> TYPE_CODEC = Identifier.CODEC.flatXmap(identifier -> {
        TextureTransformerSerializer<?> serializer = TYPES.get(identifier);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + identifier);
    }, serializer -> {
        Identifier identifier = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(identifier) : DataResult.error(() -> "Unknown type " + identifier);
    });

    public static <T extends TextureTransformer> TextureTransformerSerializer<T> register(Identifier id, TextureTransformerSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for texture transformer serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<Identifier, TextureTransformerSerializer<?>> getTypes() {
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
