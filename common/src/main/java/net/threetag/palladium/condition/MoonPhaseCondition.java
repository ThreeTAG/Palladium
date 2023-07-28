package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;

public class MoonPhaseCondition extends Condition {

    private final int min, max;

    public MoonPhaseCondition(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

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

        @Override
        public String getDocumentationDescription() {
            return "Checks if the moon phase is between the given values.";
        }
    }

}
