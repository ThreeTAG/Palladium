package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;

public class IsMovingCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        if(entity.level().isClientSide) {
            return entity.xo != entity.getX() || entity.yo != entity.getY() || entity.zo != entity.getZ();
        } else {
            return entity.walkDist != entity.walkDistO;
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IS_MOVING.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new IsMovingCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is moving.";
        }
    }
}
