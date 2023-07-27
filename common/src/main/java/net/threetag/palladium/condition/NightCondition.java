package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;

public class NightCondition extends Condition {

    @Override
    public boolean active(ConditionContext context) {
        var entity = context.get(ConditionContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.level.isNight();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.NIGHT.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new NightCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if it's currently nighttime";
        }
    }
}
