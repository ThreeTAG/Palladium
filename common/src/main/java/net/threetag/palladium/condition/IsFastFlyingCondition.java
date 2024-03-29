package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.entity.PalladiumPlayerExtension;

public class IsFastFlyingCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        if (entity instanceof PalladiumPlayerExtension extension) {
            return extension.palladium$getFlightHandler().getFlightAnimation(1F) > 1F;
        }
        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IS_FAST_FLYING.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new IsFastFlyingCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is currently flying fast.";
        }
    }
}
