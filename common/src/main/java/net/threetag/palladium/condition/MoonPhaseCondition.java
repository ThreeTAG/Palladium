package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class MoonPhaseCondition extends Condition {

    private final int min, max;

    public MoonPhaseCondition(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        return entity.level.getMoonPhase() >= this.min && entity.level.getMoonPhase() <= this.max;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.MOON_PHASE.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min_phase").configurable("Minimum phase required to be active");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max_phase").configurable("Maximum phase required to be active");

        public Serializer() {
            this.withProperty(MIN, 0);
            this.withProperty(MAX, 7);
        }

        @Override
        public Condition make(JsonObject json) {
            return new MoonPhaseCondition(this.getProperty(json, MIN), this.getProperty(json, MAX));
        }

    }

}
