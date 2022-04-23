package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.PropertyManager;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class ActivationCondition extends Condition {

    public static final PalladiumProperty<Integer> ACTIVATION_TIMER = new IntegerProperty("activation_timer");

    public final int ticks;

    public ActivationCondition(int ticks) {
        this.ticks = ticks;
    }

    @Override
    public void registerProperties(PropertyManager manager) {
        manager.register(ACTIVATION_TIMER, 0);
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        int timer = Objects.requireNonNull(entry).getProperty(ACTIVATION_TIMER);

        if (timer > 0) {
            entry.setOwnProperty(ACTIVATION_TIMER, timer - 1);
            return true;
        }

        return false;
    }

    @Override
    public boolean needsKey() {
        return true;
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        if (entry.getProperty(ACTIVATION_TIMER) <= 0) {
            entry.setOwnProperty(ACTIVATION_TIMER, this.ticks);
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
