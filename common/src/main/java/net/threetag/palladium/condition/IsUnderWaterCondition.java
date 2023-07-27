package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;

public class IsUnderWaterCondition extends Condition {

    @Override
    public boolean active(ConditionContext context) {
        var entity = context.get(ConditionContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isUnderWater();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IS_UNDER_WATER.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new IsUnderWaterCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is under water.";
        }
    }
}
