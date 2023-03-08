package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import org.jetbrains.annotations.Nullable;

public class InLavaCondition extends Condition {

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        return entity.isInLava();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IN_LAVA.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new InLavaCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is in lava.";
        }
    }
}
