package net.threetag.palladium.power.ability.enabling;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.logic.condition.Condition;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.util.Utils;

import java.util.Collections;
import java.util.List;

public class ConditionalEnablingHandler extends EnablingHandler {

    public static final MapCodec<ConditionalEnablingHandler> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Condition.LIST_CODEC.fieldOf("conditions").forGetter(handler -> handler.conditions)
    ).apply(instance, ConditionalEnablingHandler::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, ConditionalEnablingHandler> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.collection(Utils::newList, Condition.STREAM_CODEC), h -> h.conditions,
            ConditionalEnablingHandler::new
    );

    public static final ConditionalEnablingHandler EMPTY = new ConditionalEnablingHandler(Collections.emptyList());

    public final List<Condition> conditions;

    public ConditionalEnablingHandler(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public boolean check(LivingEntity entity, AbilityInstance<?> abilityInstance) {
        return Condition.checkConditions(this.conditions, DataContext.forAbility(entity, abilityInstance));
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

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ConditionalEnablingHandler> streamCodec() {
            return STREAM_CODEC;
        }
    }
}
