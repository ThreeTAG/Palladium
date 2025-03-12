package net.threetag.palladium.client.energybeam;

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

public abstract class EnergyBeamRendererSerializer<T extends EnergyBeamRenderer> implements Documented<EnergyBeamRenderer, T> {

    private static final BiMap<ResourceLocation, EnergyBeamRendererSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<EnergyBeamRendererSerializer<?>> TYPE_CODEC = ResourceLocation.CODEC.flatXmap(resourceLocation -> {
        EnergyBeamRendererSerializer<?> serializer = TYPES.get(resourceLocation);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + resourceLocation);
    }, serializer -> {
        ResourceLocation resourceLocation = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(resourceLocation) : DataResult.error(() -> "Unknown type " + resourceLocation);
    });

    public static <T extends EnergyBeamRenderer> EnergyBeamRendererSerializer<T> register(ResourceLocation id, EnergyBeamRendererSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for energy beam renderer serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<ResourceLocation, EnergyBeamRendererSerializer<?>> getTypes() {
        return ImmutableMap.copyOf(TYPES);
    }

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<EnergyBeamRenderer, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), EnergyBeamRenderer.CODEC, provider)
                .ignore("conditions");
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<EnergyBeamRenderer, T> builder, HolderLookup.Provider provider);

}
