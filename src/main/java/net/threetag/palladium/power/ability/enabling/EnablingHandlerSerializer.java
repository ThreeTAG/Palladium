package net.threetag.palladium.power.ability.enabling;

import com.mojang.serialization.MapCodec;

public abstract class EnablingHandlerSerializer<T extends EnablingHandler> {

    public abstract MapCodec<T> codec();

}
