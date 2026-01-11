package net.threetag.palladium.attachment;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.threetag.palladium.Palladium;

import java.util.function.Supplier;

public class PackAttachmentCodecs {

    private static final BiMap<Identifier, Entry<?>> TYPES = HashBiMap.create();

    public static final Codec<Entry<?>> TYPE_CODEC = Identifier.CODEC.flatXmap(identifier -> {
        Entry<?> serializer = TYPES.get(identifier);
        return serializer != null ? DataResult.success(serializer) : DataResult.error(() -> "Unknown type " + identifier);
    }, serializer -> {
        Identifier identifier = TYPES.inverse().get(serializer);
        return serializer != null ? DataResult.success(identifier) : DataResult.error(() -> "Unknown type " + identifier);
    });

    static {
        register(Palladium.id("byte"), Codec.BYTE, ByteBufCodecs.BYTE, () -> (byte) 0);
        register(Palladium.id("short"), Codec.SHORT, ByteBufCodecs.SHORT, () -> (short) 0);
        register(Palladium.id("integer"), Codec.INT, ByteBufCodecs.VAR_INT, () -> 0);
        register(Palladium.id("long"), Codec.LONG, ByteBufCodecs.LONG, () -> 0L);
        register(Palladium.id("double"), Codec.DOUBLE, ByteBufCodecs.DOUBLE, () -> 0.0D);
        register(Palladium.id("float"), Codec.FLOAT, ByteBufCodecs.FLOAT, () -> 0.0F);
        register(Palladium.id("boolean"), Codec.BOOL, ByteBufCodecs.BOOL, () -> false);
        register(Palladium.id("string"), Codec.STRING, ByteBufCodecs.STRING_UTF8, () -> "");
        register(Palladium.id("component"), ComponentSerialization.CODEC, ComponentSerialization.STREAM_CODEC, Component::empty);
    }

    public static <T> Entry<T> register(Identifier id, Codec<T> codec, StreamCodec<? extends ByteBuf, T> streamCodec, Supplier<T> defaultSupplier) {
        if (TYPES.containsKey(id)) {
            throw new IllegalStateException("Duplicate registration for attachment codec: " + id);
        }

        var entry = new Entry<>(codec, streamCodec, defaultSupplier);
        TYPES.put(id, entry);
        return entry;
    }

    public record Entry<T>(Codec<T> codec, StreamCodec<? extends ByteBuf, T> streamCodec, Supplier<T> defaultSupplier) {

    }

}
