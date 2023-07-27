package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.condition.context.ConditionContext;
import net.threetag.palladium.condition.context.ConditionContextType;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.jetbrains.annotations.Nullable;

public class AbilityOnCooldownCondition extends Condition {

    @Nullable
    private final ResourceLocation power;
    private final String abilityId;

    public AbilityOnCooldownCondition(@Nullable ResourceLocation power, String abilityId) {
        this.power = power;
        this.abilityId = abilityId;
    }

    @Override
    public boolean active(ConditionContext context) {
        var entity = context.get(ConditionContextType.ENTITY);
        var holder = context.get(ConditionContextType.POWER_HOLDER);

        if (entity == null) {
            return false;
        }

        AbilityEntry dependency = null;
        if (this.power != null) {
            dependency = AbilityUtil.getEntry(entity, this.power, this.abilityId);
        } else if (holder != null) {
            dependency = holder.getAbilities().get(this.abilityId);
        }
        return dependency != null && dependency.isOnCooldown();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_ON_COOLDOWN.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public String getDocumentationDescription() {
            return "Checks if the ability is currently on cooldown. If the power is not null, it will look for the ability in the specified power. If the power is null, it will look for the ability in the current power.";
        }

        public Serializer() {
            this.withProperty(AbilityEnabledCondition.Serializer.POWER, null);
            this.withProperty(AbilityEnabledCondition.Serializer.ABILITY, "ability_id");
        }

        @Override
        public Condition make(JsonObject json) {
            return new AbilityOnCooldownCondition(this.getProperty(json, AbilityEnabledCondition.Serializer.POWER), this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY));
        }

    }
}
