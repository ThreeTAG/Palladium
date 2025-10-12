package net.threetag.palladium.power.ability.keybind;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class AbilityKeyBind extends KeyBindType {

    public static final AbilityKeyBind INSTANCE = new AbilityKeyBind();
    public static final MapCodec<AbilityKeyBind> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityKeyBind> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    @Override
    public KeyBindTypeSerializer<?> getSerializer() {
        return KeyBindTypeSerializers.ABILITY_KEY.get();
    }

    public static class Serializer extends KeyBindTypeSerializer<AbilityKeyBind> {

        @Override
        public MapCodec<AbilityKeyBind> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityKeyBind> streamCodec() {
            return STREAM_CODEC;
        }
    }

}
