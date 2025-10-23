package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;

public record AbilityFirstTickCondition(AbilityReference ability) implements Condition {

    public static final MapCodec<AbilityFirstTickCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(AbilityReference.CODEC.fieldOf("ability").forGetter(AbilityFirstTickCondition::ability)
            ).apply(instance, AbilityFirstTickCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityFirstTickCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityFirstTickCondition::ability, AbilityFirstTickCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerHolder();

        if (entity == null) {
            return false;
        }

        AbilityInstance<?> dependency = this.ability.getInstance(entity, holder);

        if (dependency == null) {
            return false;
        } else {
            return dependency.getEnabledTicks() == 1;
        }
    }

    @Override
    public ConditionSerializer<AbilityFirstTickCondition> getSerializer() {
        return ConditionSerializers.ABILITY_FIRST_TICK.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityFirstTickCondition> {

        @Override
        public MapCodec<AbilityFirstTickCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityFirstTickCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the ability is on its first tick.";
        }
    }
}
