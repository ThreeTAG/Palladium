package net.threetag.palladium.client.renderer.entity.layer.pack;

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

public abstract class PackRenderLayerSerializer<T extends PackRenderLayer<? extends PackRenderLayer.State>> implements Documented<PackRenderLayer<? extends PackRenderLayer.State>, T> {

    private static final BiMap<Identifier, PackRenderLayerSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<PackRenderLayerSerializer<?>> TYPE_CODEC = Identifier.CODEC.flatXmap(identifier -> {
        PackRenderLayerSerializer<?> layerSerializer = TYPES.get(identifier);
        return layerSerializer != null ? DataResult.success(layerSerializer) : DataResult.error(() -> "Unknown type " + identifier);
    }, layerSerializer -> {
        Identifier identifier = TYPES.inverse().get(layerSerializer);
        return layerSerializer != null ? DataResult.success(identifier) : DataResult.error(() -> "Unknown type " + identifier);
    });

    public static <R extends PackRenderLayer.State, T extends PackRenderLayer<R>> PackRenderLayerSerializer<T> register(Identifier id, PackRenderLayerSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for pack render layer serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<Identifier, PackRenderLayerSerializer<?>> getTypes() {
        return ImmutableMap.copyOf(TYPES);
    }

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<PackRenderLayer<? extends PackRenderLayer.State>, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), PackRenderLayer.Codecs.SIMPLE_CODEC, provider);
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<PackRenderLayer<? extends PackRenderLayer.State>, T> builder, HolderLookup.Provider provider);

}
