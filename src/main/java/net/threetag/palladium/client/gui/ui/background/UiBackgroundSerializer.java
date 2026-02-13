package net.threetag.palladium.client.gui.ui.background;

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

public abstract class UiBackgroundSerializer<T extends UiBackground> implements Documented<UiBackground, T> {

    private static final BiMap<Identifier, UiBackgroundSerializer<?>> TYPES = HashBiMap.create();

    public static final Codec<UiBackgroundSerializer<?>> TYPE_CODEC = Identifier.CODEC.flatXmap(identifier -> {
        UiBackgroundSerializer<?> serializer = TYPES.get(identifier);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + identifier);
    }, serializer -> {
        Identifier identifier = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(identifier) : DataResult.error(() -> "Unknown type " + identifier);
    });

    public static <T extends UiBackground> UiBackgroundSerializer<T> register(Identifier id, UiBackgroundSerializer<T> serializer) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for UI background serializer: " + id);
        }

        TYPES.put(id, serializer);
        return serializer;
    }

    public static Map<Identifier, UiBackgroundSerializer<?>> getTypes() {
        return ImmutableMap.copyOf(TYPES);
    }

    public abstract MapCodec<T> codec();

    @Override
    public CodecDocumentationBuilder<UiBackground, T> getDocumentation(HolderLookup.Provider provider) {
        var builder = new CodecDocumentationBuilder<>(codec(), UiBackground.Codecs.CODEC, provider);
        this.addDocumentation(builder, provider);
        return builder;
    }

    public abstract void addDocumentation(CodecDocumentationBuilder<UiBackground, T> builder, HolderLookup.Provider provider);
}
