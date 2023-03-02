package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import org.jetbrains.annotations.Nullable;

public class CrouchingCondition extends Condition {

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        return entity.isCrouching();
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
        public String getDescription() {
            return "Checks if the entity is crouching.";
        }
    }

}
