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

public class PowerLostTrigger extends SimpleCriterionTrigger<PowerLostTrigger.TriggerInstance> {

    @Override
    public Codec<PowerLostTrigger.TriggerInstance> codec() {
        return PowerLostTrigger.TriggerInstance.CODEC;
    }

    public void trigger(ServerPlayer player, Identifier powerId) {
        this.trigger(player, (triggerInstance) -> triggerInstance.matches(powerId));
    }

    public record TriggerInstance(Optional<ContextAwarePredicate> player,
                                  Optional<IdentifierPredicate> power) implements SimpleInstance {

        public static final Codec<PowerLostTrigger.TriggerInstance> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(PowerLostTrigger.TriggerInstance::player),
                IdentifierPredicate.CODEC.optionalFieldOf("power").forGetter(PowerLostTrigger.TriggerInstance::power)
        ).apply(instance, PowerLostTrigger.TriggerInstance::new));

        public static Criterion<PowerLostTrigger.TriggerInstance> powerLost(@Nullable String namespace, @Nullable String path) {
            return PalladiumCriteriaTriggers.POWER_LOST.get().createCriterion(new PowerLostTrigger.TriggerInstance(Optional.empty(), Optional.of(new IdentifierPredicate(Optional.ofNullable(namespace), Optional.ofNullable(path)))));
        }

        public static Criterion<PowerLostTrigger.TriggerInstance> powerLost(String namespace) {
            return powerLost(namespace, null);
        }

        public static Criterion<PowerLostTrigger.TriggerInstance> powerLost() {
            return PalladiumCriteriaTriggers.POWER_LOST.get().createCriterion(new PowerLostTrigger.TriggerInstance(Optional.empty(), Optional.empty()));
        }

        public boolean matches(Identifier powerId) {
            return this.power.isEmpty() || this.power.get().test(powerId);
        }
    }
}
