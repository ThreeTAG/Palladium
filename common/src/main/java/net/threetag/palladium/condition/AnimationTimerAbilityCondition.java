package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import net.threetag.palladium.power.ability.AnimationTimer;
import net.threetag.palladium.util.context.DataContext;
import net.threetag.palladium.util.property.IntegerProperty;
import net.threetag.palladium.util.property.PalladiumProperty;
import net.threetag.palladium.util.property.StringProperty;
import org.jetbrains.annotations.Nullable;

public class AnimationTimerAbilityCondition extends Condition {

    @Nullable
    private final ResourceLocation power;
    private final String abilityId;
    private final int min, max;

    public AnimationTimerAbilityCondition(@Nullable ResourceLocation power, String abilityId, int min, int max) {
        this.power = power;
        this.abilityId = abilityId;
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean active(DataContext context) {
        var entity = context.getLivingEntity();
        var holder = context.getPowerHolder();

        if (entity == null) {
            return false;
        }

        AbilityEntry dependency = null;
        if (this.power != null) {
            dependency = AbilityUtil.getEntry(entity, this.power, this.abilityId);
        } else if (holder != null) {
            dependency = holder.getAbilities().get(this.abilityId);
        }

        if (dependency == null || !(dependency.getConfiguration().getAbility() instanceof AnimationTimer animationTimer)) {
            return false;
        }

        var timer = (int) animationTimer.getAnimationTimer(dependency, 1F);
        return timer >= this.min && timer <= this.max;
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ANIMATION_TIMER_ABILITY.get();
    }

    public static class Serializer extends ConditionSerializer {

        public static final PalladiumProperty<Integer> MIN = new IntegerProperty("min").configurable("Minimum required amount of the timer value");
        public static final PalladiumProperty<Integer> MAX = new IntegerProperty("max").configurable("Maximum required amount of the timer value");

        public Serializer() {
            this.withProperty(AbilityEnabledCondition.Serializer.POWER, null);
            this.withProperty(AbilityEnabledCondition.Serializer.ABILITY, "ability_id");
            this.withProperty(MIN, 0);
            this.withProperty(MAX, 0);
        }

        @Override
        public Condition make(JsonObject json) {
            return new AnimationTimerAbilityCondition(this.getProperty(json, AbilityEnabledCondition.Serializer.POWER),
                    this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY),
                    this.getProperty(json, MIN),
                    this.getProperty(json, MAX));
        }

        @Override
        public String getDocumentationDescription() {
            return "Checks if the given animation timer ability has a certain value. This condition is a simplified version of the ability_integer_property condition designed to be used for animation timer abilities.";
        }
    }
}
