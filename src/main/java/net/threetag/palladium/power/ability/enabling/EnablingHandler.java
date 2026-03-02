package net.threetag.palladium.power.ability.enabling;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.registry.PalladiumRegistries;

public abstract class EnablingHandler {

    public static final Codec<EnablingHandler> DIRECT_CODEC = PalladiumRegistries.ABILITY_ENABLING_HANDLER_SERIALIZER.byNameCodec().dispatch(EnablingHandler::getSerializer, EnablingHandlerSerializer::codec);

    public static final Codec<EnablingHandler> CODEC = Codec.either(
            DIRECT_CODEC,
            Condition.CODEC
    ).xmap(
            either -> either.map(
                    left -> left,
                    ConditionalEnablingHandler::new
            ),
            handler -> handler instanceof ConditionalEnablingHandler cond ? Either.right(cond.condition) : Either.left(handler)
    );

    public abstract boolean check(LivingEntity entity, AbilityInstance<?> abilityInstance);

    public void tick(LivingEntity entity, AbilityInstance<?> abilityInstance, boolean enabled) {
    }

    public void registerDataComponents(DataComponentMap.Builder builder) {
    }

    public void onUnlocked(LivingEntity entity, AbilityInstance<?> abilityInstance) {
    }

    public boolean isKeyBound() {
        return false;
    }

    public abstract EnablingHandlerSerializer<?> getSerializer();

}
