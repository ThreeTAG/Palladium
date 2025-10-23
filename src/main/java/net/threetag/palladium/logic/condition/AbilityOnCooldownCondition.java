package net.threetag.palladium.logic.condition;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.power.ability.AbilityInstance;
import net.threetag.palladium.power.ability.AbilityReference;
import net.threetag.palladium.power.ability.enabling.KeyBindEnablingHandler;

public record AbilityOnCooldownCondition(AbilityReference ability) implements Condition {

    public static final MapCodec<AbilityOnCooldownCondition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
            .group(AbilityReference.CODEC.fieldOf("ability").forGetter(AbilityOnCooldownCondition::ability)
            ).apply(instance, AbilityOnCooldownCondition::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, AbilityOnCooldownCondition> STREAM_CODEC = StreamCodec.composite(
            AbilityReference.STREAM_CODEC, AbilityOnCooldownCondition::ability, AbilityOnCooldownCondition::new
    );

    @Override
    public boolean test(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerHolder();

        if (entity == null) {
            return false;
        }

        AbilityInstance<?> dependency = this.ability.getInstance(entity, holder);

        return dependency != null
                && dependency.getAbility().getStateManager().getEnablingHandler() instanceof KeyBindEnablingHandler key
                && key.getCooldownPercentage(dependency) > 0.0F;
    }

    @Override
    public ConditionSerializer<AbilityOnCooldownCondition> getSerializer() {
        return ConditionSerializers.ABILITY_ON_COOLDOWN.get();
    }

    public static class Serializer extends ConditionSerializer<AbilityOnCooldownCondition> {

        @Override
        public MapCodec<AbilityOnCooldownCondition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, AbilityOnCooldownCondition> streamCodec() {
            return STREAM_CODEC;
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the ability is currently on cooldown. If the power is not null, it will look for the ability in the specified power. If the power is null, it will look for the ability in the current power.";
        }

    }
}
