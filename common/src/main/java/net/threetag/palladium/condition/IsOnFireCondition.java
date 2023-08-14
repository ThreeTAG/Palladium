package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class IsOnFireCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        return entity.isOnFire();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IS_ON_FIRE.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new IsOnFireCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is on fire.";
        }
    }
}
