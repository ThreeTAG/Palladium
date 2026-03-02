package net.threetag.palladium.power.ability.enabling;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.condition.TrueCondition;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;

public class ConditionalEnablingHandler extends EnablingHandler {

    public static final MapCodec<ConditionalEnablingHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Condition.CODEC.fieldOf("conditions").forGetter(handler -> handler.condition)
    ).apply(instance, ConditionalEnablingHandler::new));

    public static final ConditionalEnablingHandler EMPTY = new ConditionalEnablingHandler(TrueCondition.INSTANCE);

    public final Condition condition;

    public ConditionalEnablingHandler(Condition condition) {
        this.condition = condition;
    }

    @Override
    public boolean check(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        return this.condition.test(DataContext.forAbility(entity, abilityInstance));
    }

    @Override
    public EnablingHandlerSerializer<?> getSerializer() {
        return EnablingHandlerSerializers.CONDITIONAL.get();
    }

    public static class Serializer extends EnablingHandlerSerializer<ConditionalEnablingHandler> {

        @Override
        public MapCodec<ConditionalEnablingHandler> codec() {
            return CODEC;
        }

    }
}
