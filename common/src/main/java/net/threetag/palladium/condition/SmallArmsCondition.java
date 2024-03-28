package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.threetag.palladium.util.PlayerUtil;
import net.threetag.palladium.util.context.DataContext;

public class SmallArmsCondition extends Condition {

    @Override
    public boolean active(DataContext context) {
        Entity entity = context.getEntity();
        if (!(entity instanceof Player player)) return false;
        return PlayerUtil.hasSmallArms(player);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.SMALL_ARMS.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new SmallArmsCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity has small arms. Returns false if the entity is not a player or if this condition is being checked sever-side.";
        }
    }
}
