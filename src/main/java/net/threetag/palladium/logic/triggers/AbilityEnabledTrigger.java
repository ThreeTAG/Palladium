package net.threetag.palladium.logic.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.threetag.palladium.power.ability.AbilityReference;

import java.util.Optional;

public class AbilityEnabledTrigger extends SimpleCriterionTrigger<AbilityEnabledTrigger.TriggerInstance> {

    @Override
    public Codec<AbilityEnabledTrigger.TriggerInstance> codec() {
        return AbilityEnabledTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, AbilityReference abilityReference) {
        this.trigger(player, (triggerInstance) -> triggerInstance.matches(abilityReference));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  Optional<AbilityReference> ability) implements SimpleInstance {

        public static final Codec<AbilityEnabledTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(AbilityEnabledTrigger.TriggerInstance::player),
                AbilityReference.CODEC.optionalFieldOf("ability").forGetter(AbilityEnabledTrigger.TriggerInstance::ability)
        ).apply(instance, AbilityEnabledTrigger.TriggerInstance::new));

        public static Criterion<AbilityEnabledTrigger.TriggerInstance> abilityEnabled(AbilityReference abilityReference) {
            return PalladiumCriteriaTriggers.ABILITY_ENABLED.get().createCriterion(new AbilityEnabledTrigger.TriggerInstance(Optional.empty(), Optional.of(abilityReference)));
        }

        public boolean matches(AbilityReference abilityReference) {
            return this.ability.isEmpty() || this.ability.get().equals(abilityReference);
        }
    }
}
