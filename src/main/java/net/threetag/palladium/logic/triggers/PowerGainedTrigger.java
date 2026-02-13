package net.threetag.palladium.logic.triggers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class PowerGainedTrigger extends SimpleCriterionTrigger<PowerGainedTrigger.TriggerInstance> {

    @Override
    public Codec<PowerGainedTrigger.TriggerInstance> codec() {
        return PowerGainedTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Identifier powerId) {
        this.trigger(player, (triggerInstance) -> triggerInstance.matches(powerId));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  Optional<IdentifierPredicate> power) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<PowerGainedTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(PowerGainedTrigger.TriggerInstance::player),
                IdentifierPredicate.CODEC.optionalFieldOf("power").forGetter(PowerGainedTrigger.TriggerInstance::power)
        ).apply(instance, PowerGainedTrigger.TriggerInstance::new));

        public static Criterion<PowerGainedTrigger.TriggerInstance> powerGained(@Nullable String namespace, @Nullable String path) {
            return PalladiumCriteriaTriggers.POWER_GAINED.get().createCriterion(new PowerGainedTrigger.TriggerInstance(Optional.empty(), Optional.of(new IdentifierPredicate(Optional.ofNullable(namespace), Optional.ofNullable(path)))));
        }

        public static Criterion<PowerGainedTrigger.TriggerInstance> powerGained(String namespace) {
            return powerGained(namespace, null);
        }

        public static Criterion<PowerGainedTrigger.TriggerInstance> powerGained() {
            return PalladiumCriteriaTriggers.POWER_GAINED.get().createCriterion(new PowerGainedTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
        }

        public boolean matches(Identifier powerId) {
            return this.power.isEmpty() || this.power.get().test(powerId);
        }
    }
}
