package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class IsInWaterOrBubbleCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isInWaterOrBubble();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IS_IN_WATER_OR_BUBBLE.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new IsInWaterOrBubbleCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in water or a bubble column.";
        }
    }
}
