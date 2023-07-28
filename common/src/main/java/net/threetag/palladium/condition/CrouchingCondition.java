package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class CrouchingCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);
        return entity != null && entity.isCrouching();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.CROUCHING.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new CrouchingCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is crouching.";
        }
    }

}
