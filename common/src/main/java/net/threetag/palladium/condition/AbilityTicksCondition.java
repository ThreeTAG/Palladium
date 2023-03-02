package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import org.jetbrains.annotations.Nullable;

public class AbilityTicksCondition extends Condition {

    @Nullable
    private final ResourceLocation power;
    private final String abilityId;
    private final int min, max;

    public AbilityTicksCondition(@Nullable ResourceLocation power, String abilityId, int min, int max) {
        this.power = power;
        this.abilityId = abilityId;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder) {
        AbilityEntry dependency = null;
        if (this.power != null) {
            dependency = AbilityUtil.getEntry(entity, this.power, this.abilityId);
        } else if (holder != null) {
            dependency = holder.getAbilities().get(this.abilityId);
        }

        if (dependency == null) {
            return false;
        } else {
            return this.min <= dependency.getEnabledTicks() && dependency.getEnabledTicks() <= this.max;
        }
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_TICKS.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min").configurable("Minimum required amount of enabled ticks");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max").configurable("Maximum required amount of enabled ticks");

        public Serializer() {
            this.withProperty(AbilityEnabledCondition.Serializer.POWER, null);
            this.withProperty(AbilityEnabledCondition.Serializer.ABILITY, "ability_id");
            this.withProperty(MIN, 0);
            this.withProperty(MAX, 0);
        }

        @Override
        public Condition make(JsonObject json) {
            return new AbilityTicksCondition(this.getProperty(json, AbilityEnabledCondition.Serializer.POWER),
                    this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY),
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the ability has been enabled for a certain amount of ticks.";
        }
    }
}
