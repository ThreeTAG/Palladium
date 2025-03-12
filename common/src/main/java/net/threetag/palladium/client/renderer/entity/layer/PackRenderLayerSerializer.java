package net.threetag.palladium.client.renderer.entity.layer;

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

public abstract class PackRenderLayerSerializer<T extends PackRenderLayer<? extends PackRenderLayer.State>> implements Documented<PackRenderLayer<? extends PackRenderLayer.State>, T> {

    private static final BiMap<ResourceLocation, PackRenderLayerSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<PackRenderLayerSerializer<?>> TYPE_CODEC = ResourceLocation.CODEC.flatXmap(resourceLocation -> {
        PackRenderLayerSerializer<?> layerSerializer = TYPES.get(resourceLocation);
        return layerSerializer != null ? DataResult.success(layerSerializer) : DataResult.error(() -> "Unknown type " + resourceLocation);
    }, layerSerializer -> {
        ResourceLocation resourceLocation = TYPES.inverse().get(layerSerializer);
        return layerSerializer != null ? DataResult.success(resourceLocation) : DataResult.error(() -> "Unknown type " + resourceLocation);
    });

    public static <R extends PackRenderLayer.State, T extends PackRenderLayer<R>> PackRenderLayerSerializer<T> register(ResourceLocation id, PackRenderLayerSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for pack render layer serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<ResourceLocation, PackRenderLayerSerializer<?>> getTypes() {
        return ImmutableMap.copyOf(TYPES);
    }

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<PackRenderLayer<? extends PackRenderLayer.State>, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), PackRenderLayer.CODEC, provider);
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<PackRenderLayer<? extends PackRenderLayer.State>, T> builder, HolderLookup.Provider provider);

}
