package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;

public class ObjectiveScoreCondition extends Condition {

    private final String objectiveName;
    private final float min, max;

    public ObjectiveScoreCondition(String objectiveName, float min, float max) {
        this.objectiveName = objectiveName;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        var objective = entity.level.getScoreboard().getObjective(this.objectiveName);

        if (objective != null) {
            if (!entity.level.getScoreboard().hasPlayerScore(entity.getScoreboardName(), objective)) {
                return false;
            } else {
                int score = entity.level.getScoreboard().getOrCreatePlayerScore(entity.getScoreboardName(), objective).getScore();
                return score >= this.min && score <= this.max;
            }
        }

        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.HEALTH.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> OBJECTIVE = new StringProperty("objective").configurable("Name of the objective");
        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min_score").configurable("Minimum required score of objective");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max_score").configurable("Maximum required score of objective");

        public Serializer() {
            this.withProperty(OBJECTIVE, "objective_name");
            this.withProperty(MIN, Integer.MIN_VALUE);
            this.withProperty(MAX, Integer.MAX_VALUE);
        }

        @Override
        public Condition make(JsonObject json) {
            return new ObjectiveScoreCondition(this.getProperty(json, OBJECTIVE), this.getProperty(json, MIN), this.getProperty(json, MAX));
        }

    }

}
