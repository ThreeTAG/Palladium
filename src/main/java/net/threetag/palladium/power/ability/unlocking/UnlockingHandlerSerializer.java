package net.threetag.palladium.power.ability.unlocking;

import com.mojang.serialization.MapCodec;

public abstract class UnlockingHandlerSerializer<T extends UnlockingHandler> {

    public abstract MapCodec<T> codec();

}
