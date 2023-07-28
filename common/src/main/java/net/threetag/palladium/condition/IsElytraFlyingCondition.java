package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class IsElytraFlyingCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();

        if (entity == null) {
            return false;
        }

        return entity.isFallFlying();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IS_ELYTRA_FLYING.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new IsElytraFlyingCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is currently flying with an elytra.";
        }
    }
}
