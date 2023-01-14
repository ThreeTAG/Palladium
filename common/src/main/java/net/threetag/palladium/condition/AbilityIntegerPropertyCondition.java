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
import net.threetag.palladium.util.property.StringProperty;
import org.jetbrains.annotations.Nullable;

public class AbilityIntegerPropertyCondition extends Condition {

    @Nullable
    private final ResourceLocation power;
    private final String abilityId;
    private final String propertyKey;
    private final int min, max;

    public AbilityIntegerPropertyCondition(@Nullable ResourceLocation power, String abilityId, String propertyKey, int min, int max) {
        this.power = power;
        this.abilityId = abilityId;
        this.propertyKey = propertyKey;
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
        }

        PalladiumProperty<?> property = dependency.getEitherPropertyByKey(this.propertyKey);

        if (property instanceof IntegerProperty integerProperty) {
            int value = dependency.getProperty(integerProperty);
            return value >= this.min && value <= this.max;
        }

        return false;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_INTEGER_PROPERTY.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<String> PROPERTY = new StringProperty("property").configurable("Name of the integer property in the ability. For interpolated_integer abilities it's 'value'");
        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min").configurable("Minimum required amount of the property value");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max").configurable("Maximum required amount of the property value");

        public Serializer() {
            this.withProperty(AbilityEnabledCondition.Serializer.POWER, null);
            this.withProperty(AbilityEnabledCondition.Serializer.ABILITY, "ability_id");
            this.withProperty(PROPERTY, "value");
            this.withProperty(MIN, 0);
            this.withProperty(MAX, 0);
        }

        @Override
        public Condition make(JsonObject json) {
            return new AbilityIntegerPropertyCondition(this.getProperty(json, AbilityEnabledCondition.Serializer.POWER),
                    this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY),
                    this.getProperty(json, PROPERTY),
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

    }
}
