package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;

public class OnGroundCondition extends Condition {

    @Override
    public boolean active(ConditionContext context) {
        var entity = context.get(ConditionContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isOnGround();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ON_GROUND.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new OnGroundCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is on the ground.";
        }
    }
}
