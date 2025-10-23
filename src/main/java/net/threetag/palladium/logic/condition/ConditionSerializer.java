package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public abstract class ConditionSerializer<T extends Condition> {

    public abstract MapCodec<T> codec();

    public abstract StreamCodec<RegistryFriendlyByteBuf, T> streamCodec();

    public ConditionEnvironment getContextEnvironment() {
        return ConditionEnvironment.ALL;
    }

    public String getDocumentationDescription() {
        return "";
    }

}
