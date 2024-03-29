package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.context.DataContextType;
import net.threetag.palladium.entity.PalladiumPlayerExtension;

public class IsHoveringCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var entity = context.get(DataContextType.ENTITY);

        if (entity == null) {
            return false;
        }

        if (entity instanceof PalladiumPlayerExtension extension) {
            return extension.palladium$getFlightHandler().getHoveringAnimation(1F) > 0F;
        }
        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IS_HOVERING.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new IsHoveringCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is hovering mid-air.";
        }
    }
}
