package net.threetag.palladium.client.trail;

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

public abstract class TrailRendererSerializer<T extends TrailRenderer<?>> implements Documented<TrailRenderer<?>, T> {

    private static final BiMap<Identifier, TrailRendererSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<TrailRendererSerializer<?>> TYPE_CODEC = Identifier.CODEC.flatXmap(identifier -> {
        TrailRendererSerializer<?> serializer = TYPES.get(identifier);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + identifier);
    }, serializer -> {
        Identifier identifier = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(identifier) : DataResult.error(() -> "Unknown type " + identifier);
    });

    public static <T extends TrailRenderer<?>> TrailRendererSerializer<T> register(Identifier id, TrailRendererSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for trail renderer serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<Identifier, TrailRendererSerializer<?>> getTypes() {
        return ImmutableMap.copyOf(TYPES);
    }

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<TrailRenderer<?>, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), TrailRenderer.CODEC, provider)
                .ignore("conditions");
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<TrailRenderer<?>, T> builder, HolderLookup.Provider provider);

}