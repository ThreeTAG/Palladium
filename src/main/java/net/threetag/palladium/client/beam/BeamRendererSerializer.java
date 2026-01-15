package net.threetag.palladium.client.beam;

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

public abstract class BeamRendererSerializer<T extends BeamRenderer> implements Documented<BeamRenderer, T> {

    private static final BiMap<Identifier, BeamRendererSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<BeamRendererSerializer<?>> TYPE_CODEC = Identifier.CODEC.flatXmap(identifier -> {
        BeamRendererSerializer<?> serializer = TYPES.get(identifier);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + identifier);
    }, serializer -> {
        Identifier identifier = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(identifier) : DataResult.error(() -> "Unknown type " + identifier);
    });

    public static <T extends BeamRenderer> BeamRendererSerializer<T> register(Identifier id, BeamRendererSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for beam renderer serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<Identifier, BeamRendererSerializer<?>> getTypes() {
        return ImmutableMap.copyOf(TYPES);
    }

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<BeamRenderer, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), BeamRenderer.CODEC, provider)
                .ignore("conditions");
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<BeamRenderer, T> builder, HolderLookup.Provider provider);

}
