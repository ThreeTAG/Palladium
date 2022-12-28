package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;

public class IsMovingCondition extends Condition {

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        if(entity.level.isClientSide) {
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

    }

}
