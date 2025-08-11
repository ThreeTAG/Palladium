package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.PalladiumProperties;

public class HasMovementInputCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        var player = context.getPlayer();

        if (player != null) {
            return PalladiumProperties.JUMP_KEY_DOWN.get(player) ||
                    PalladiumProperties.LEFT_KEY_DOWN.get(player) ||
                    PalladiumProperties.RIGHT_KEY_DOWN.get(player) ||
                    PalladiumProperties.FORWARD_KEY_DOWN.get(player) ||
                    PalladiumProperties.BACKWARDS_KEY_DOWN.get(player);
        }

        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.HAS_MOVEMENT_INPUT.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new HasMovementInputCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the player is moving by itself by using their buttons.";
        }
    }
}
