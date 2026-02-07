package net.threetag.palladium.client.gui.ui.layout;

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

public abstract class UiLayoutSerializer<T extends UiLayout> implements Documented<UiLayout, T> {

    private static final BiMap<Identifier, UiLayoutSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<UiLayoutSerializer<?>> TYPE_CODEC = Identifier.CODEC.flatXmap(identifier -> {
        net.threetag.palladium.client.gui.ui.layout.UiLayoutSerializer<?> serializer = TYPES.get(identifier);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + identifier);
    }, serializer -> {
        Identifier identifier = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(identifier) : DataResult.error(() -> "Unknown type " + identifier);
    });

    public static <T extends UiLayout> UiLayoutSerializer<T> register(Identifier id, UiLayoutSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for UI layout serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<Identifier, UiLayoutSerializer<?>> getTypes() {
        return ImmutableMap.copyOf(TYPES);
    }

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<UiLayout, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), UiLayout.Codecs.CODEC, provider);
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<UiLayout, T> builder, HolderLookup.Provider provider);
}
