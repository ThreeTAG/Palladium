package net.threetag.palladium.client.renderer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

import java.util.List;

public class RenderTypeRegistry {

    private static final BiMap<Identifier, RenderTypeFunction> RENDER_TYPES = HashBiMap.create();
    public static final Codec<RenderTypeFunction> CODEC = Identifier.CODEC.flatXmap(identifier -> {
        RenderTypeFunction layerSerializer = RENDER_TYPES.get(identifier);
        return layerSerializer != null ? DataResult.success(layerSerializer) : DataResult.error(() -> "Unknown type " + identifier);
    }, layerSerializer -> {
        Identifier identifier = RENDER_TYPES.inverse().get(layerSerializer);
        return layerSerializer != null ? DataResult.success(identifier) : DataResult.error(() -> "Unknown type " + identifier);
    });

    public static RenderTypeFunction register(Identifier id, RenderTypeFunction function) {
        if (RENDER_TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for render type function: " + id);
        }

        RENDER_TYPES.put(id, function);
        return function;
    }

    public static RenderTypeFunction get(Identifier id) {
        return RENDER_TYPES.get(id);
    }

    public static Identifier getKey(RenderTypeFunction function) {
        return RENDER_TYPES.inverse().get(function);
    }

    public static List<Identifier> types() {
        return List.copyOf(RENDER_TYPES.keySet());
    }

    // TODO keep MC namespace?

    public static final RenderTypeFunction ENTITY_TRANSLUCENT = register(Identifier.withDefaultNamespace("entity_translucent"), RenderTypes::entityTranslucent);
    public static final RenderTypeFunction ENTITY_CUTOUT = register(Identifier.withDefaultNamespace("entity_cutout"), RenderTypes::entityCutout);
}
