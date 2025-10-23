package net.threetag.palladium.power.ability.unlocking;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public abstract class UnlockingHandlerSerializer<T extends UnlockingHandler> {

    public abstract MapCodec<T> codec();

    public abstract StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

}
