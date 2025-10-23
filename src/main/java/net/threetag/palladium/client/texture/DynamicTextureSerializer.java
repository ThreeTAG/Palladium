package net.threetag.palladium.client.texture;

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

public abstract class DynamicTextureSerializer<T extends DynamicTexture> implements Documented<DynamicTexture, T> {

    private static final BiMap<ResourceLocation, DynamicTextureSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<DynamicTextureSerializer<?>> TYPE_CODEC = ResourceLocation.CODEC.flatXmap(resourceLocation -> {
        DynamicTextureSerializer<?> serializer = TYPES.get(resourceLocation);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + resourceLocation);
    }, serializer -> {
        ResourceLocation resourceLocation = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(resourceLocation) : DataResult.error(() -> "Unknown type " + resourceLocation);
    });

    public static <T extends DynamicTexture> DynamicTextureSerializer<T> register(ResourceLocation id, DynamicTextureSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for dynamic texture serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<ResourceLocation, DynamicTextureSerializer<?>> getTypes() {
        return ImmutableMap.copyOf(TYPES);
    }

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<DynamicTexture, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), DynamicTexture.Codecs.SIMPLE_CODEC, provider);
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<DynamicTexture, T> builder, HolderLookup.Provider provider);

}
