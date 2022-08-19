package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHandler;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.PowerManager;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.ResourceLocationProperty;
import org.jetbrains.annotations.Nullable;

public class HasPowerCondition extends Condition {

    public final ResourceLocation powerId;

    public HasPowerCondition(ResourceLocation powerId) {
        this.powerId = powerId;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        IPowerHandler handler = PowerManager.getPowerHandler(entity).orElse(null);
        return handler != null && handler.getPowerHolders().containsKey(this.powerId);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.HAS_POWER.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<ResourceLocation> POWER = new ResourceLocationProperty("power").configurable("ID of the power that is required");

        public Serializer() {
            this.withProperty(POWER, new ResourceLocation("example:power_id"));
        }

        @Override
        public Condition make(JsonObject json) {
            return new HasPowerCondition(this.getProperty(json, POWER));
        }

    }
}
