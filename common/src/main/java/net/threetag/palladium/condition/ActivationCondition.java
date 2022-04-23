package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ActivationCondition extends Condition {

    public final int ticks;

    public ActivationCondition(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        return Objects.requireNonNull(entry).cooldown > 0;
    }

    @Override
    public boolean needsKey() {
        return true;
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        if (entry.cooldown <= 0) {
            entry.maxCooldown = entry.cooldown = this.ticks;
            entry.syncState(entity);
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ACTIVATION.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> TICKS = new IntegerProperty("ticks").configurable("The amount of ticks the ability will be active for");

        public Serializer() {
            this.withProperty(TICKS, 60);
        }

        @Override
        public Condition make(JsonObject json) {
            return new ActivationCondition(this.getProperty(json, TICKS));
        }

        @Override
        public ConditionContextType getContextType() {
            return ConditionContextType.ABILITIES;
        }
    }
}
