package net.threetag.palladium.client.renderer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.renderer.entity.layer.RenderTypeFunction;

import java.util.List;

public class RenderTypeRegistry {

    private static final BiMap<ResourceLocation, RenderTypeFunction> RENDER_TYPES = HashBiMap.create();
    public static final Codec<RenderTypeFunction> CODEC = ResourceLocation.CODEC.flatXmap(resourceLocation -> {
        RenderTypeFunction layerSerializer = RENDER_TYPES.get(resourceLocation);
        return layerSerializer != null ? DataResult.success(layerSerializer) : DataResult.error(() -> "Unknown type " + resourceLocation);
    }, layerSerializer -> {
        ResourceLocation resourceLocation = RENDER_TYPES.inverse().get(layerSerializer);
        return layerSerializer != null ? DataResult.success(resourceLocation) : DataResult.error(() -> "Unknown type " + resourceLocation);
    });

    public static RenderTypeFunction register(ResourceLocation id, RenderTypeFunction function) {
        if (RENDER_TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for render type function: " + id);
        }

        RENDER_TYPES.put(id, function);
        return function;
    }

    public static RenderTypeFunction get(ResourceLocation id) {
        return RENDER_TYPES.get(id);
    }

    public static ResourceLocation getKey(RenderTypeFunction function) {
        return RENDER_TYPES.inverse().get(function);
    }

    public static List<ResourceLocation> types() {
        return List.copyOf(RENDER_TYPES.keySet());
    }

    // TODO keep MC namespace?

    public static final RenderTypeFunction ENTITY_TRANSLUCENT = register(ResourceLocation.withDefaultNamespace("entity_translucent"), RenderType::entityTranslucent);
    public static final RenderTypeFunction ENTITY_CUTOUT = register(ResourceLocation.withDefaultNamespace("entity_cutout"), RenderType::entityCutout);
}
