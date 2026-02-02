package net.threetag.palladium.client.gui.ui.component;

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

public abstract class UiComponentSerializer<T extends UiComponent> implements Documented<UiComponent, T> {

    private static final BiMap<Identifier, UiComponentSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<UiComponentSerializer<?>> TYPE_CODEC = Identifier.CODEC.flatXmap(identifier -> {
        UiComponentSerializer<?> serializer = TYPES.get(identifier);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + identifier);
    }, serializer -> {
        Identifier identifier = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(identifier) : DataResult.error(() -> "Unknown type " + identifier);
    });

    public static <T extends UiComponent> UiComponentSerializer<T> register(Identifier id, UiComponentSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for PalladUI component serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<Identifier, UiComponentSerializer<?>> getTypes() {
        return ImmutableMap.copyOf(TYPES);
    }

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<UiComponent, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), UiComponent.CODEC, provider)
                .addOptional("properties", TYPE_UI_PROPERTIES, "Properties of this component", UiComponentProperties.DEFAULT);
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<UiComponent, T> builder, HolderLookup.Provider provider);
}
