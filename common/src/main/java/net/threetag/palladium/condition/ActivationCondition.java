package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ActivationCondition extends KeyCondition {

    public final int ticks;

    public ActivationCondition(int ticks, int cooldown, AbilityConfiguration.KeyType type, boolean needsEmptyHand) {
        super(cooldown, type, needsEmptyHand);
        this.ticks = ticks;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        if (this.cooldown != 0 && Objects.requireNonNull(entry).activationTimer == 1) {
            entry.startCooldown(entity, this.cooldown);
        }
        return Objects.requireNonNull(entry).activationTimer > 0;
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        if (entry.cooldown <= 0 && entry.activationTimer == 0) {
            entry.startActivationTimer(entity, this.ticks);
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ACTIVATION.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> TICKS = new IntegerProperty("ticks").configurable("The amount of ticks the ability will be active for");

        public Serializer() {
            this.withProperty(ActionCondition.Serializer.COOLDOWN, 0);
            this.withProperty(TICKS, 60);
            this.withProperty(KeyCondition.KEY_TYPE, AbilityConfiguration.KeyType.KEY_BIND);
            this.withProperty(KeyCondition.NEEDS_EMPTY_HAND, false);
        }

        @Override
        public Condition make(JsonObject json) {
            return new ActivationCondition(this.getProperty(json, TICKS), this.getProperty(json, ActionCondition.Serializer.COOLDOWN), this.getProperty(json, KeyCondition.KEY_TYPE), this.getProperty(json, KeyCondition.NEEDS_EMPTY_HAND));
        }

        @Override
        public ConditionContextType getContextType() {
            return ConditionContextType.ABILITIES;
        }
    }
}
