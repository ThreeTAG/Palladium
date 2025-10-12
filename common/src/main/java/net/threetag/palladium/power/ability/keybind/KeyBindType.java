package net.threetag.palladium.power.ability.keybind;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.registry.PalladiumRegistries;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public abstract class KeyBindType {

    public static final Codec<KeyBindType> CODEC = PalladiumRegistries.KEY_BIND_TYPE_SERIALIZER.byNameCodec().dispatch(KeyBindType::getSerializer, KeyBindTypeSerializer::codec);

    public static final StreamCodec<RegistryFriendlyByteBuf, KeyBindType> STREAM_CODEC = ByteBufCodecs.registry(PalladiumRegistryKeys.KEY_BIND_TYPE_SERIALIZER).dispatch(KeyBindType::getSerializer, KeyBindTypeSerializer::streamCodec);

    public abstract KeyBindTypeSerializer<?> getSerializer();

}
