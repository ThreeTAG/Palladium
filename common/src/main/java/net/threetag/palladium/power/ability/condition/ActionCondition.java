package net.threetag.palladium.power.ability.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;

public class ActionCondition extends Condition {

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        if (entry.keyPressed) {
            entry.keyPressed = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean needsKey() {
        return true;
    }

    @Override
    public void onKeyPressed(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        entry.keyPressed = true;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ACTION.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new ActionCondition();
        }

    }

}
