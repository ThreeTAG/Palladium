package net.threetag.palladium.power.ability.keybind;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public abstract class KeyBindTypeSerializer<T extends KeyBindType> {

    public abstract MapCodec<T> codec();

    public abstract StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

}
