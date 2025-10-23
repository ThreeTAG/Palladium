package net.threetag.palladium.power.ability.enabling;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public abstract class EnablingHandlerSerializer<T extends EnablingHandler> {

    public abstract MapCodec<T> codec();

    public abstract StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

}
