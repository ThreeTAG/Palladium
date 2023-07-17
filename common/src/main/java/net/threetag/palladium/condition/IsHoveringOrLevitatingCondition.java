package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.entity.PalladiumPlayerExtension;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import org.jetbrains.annotations.Nullable;

public class IsHoveringOrLevitatingCondition extends Condition {

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        if (entity instanceof PalladiumPlayerExtension extension) {
            float flight = extension.palladium$getFlightHandler().getFlightAnimation(1F);
            return extension.palladium$getFlightHandler().getHoveringAnimation(1F) > 0F || (flight > 0F && flight <= 1F);
        }
        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.IS_HOVERING_OR_LEVITATING.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new IsHoveringOrLevitatingCondition();
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the entity is hovering mid-air or levitating.";
        }
    }
}
