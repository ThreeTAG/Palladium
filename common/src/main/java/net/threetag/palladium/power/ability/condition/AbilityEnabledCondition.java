package net.threetag.palladium.power.ability.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.Ability;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.json.GsonUtil;
import org.jetbrains.annotations.Nullable;

public class AbilityEnabledCondition extends Condition {

    @Nullable
    private final ResourceLocation power;
    private final String abilityId;

    public AbilityEnabledCondition(@Nullable ResourceLocation power, String abilityId) {
        this.power = power;
        this.abilityId = abilityId;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        AbilityEntry dependency;
        if(this.power != null) {
            dependency = Ability.getEntry(entity, this.power, this.abilityId);
        } else {
            dependency = holder.getAbilities().get(this.abilityId);
        }
        return dependency != null && dependency.isEnabled();
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_ENABLED.get();
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition make(JsonObject json) {
            return new AbilityEnabledCondition(GsonUtil.getAsResourceLocation(json, "power", null), GsonHelper.getAsString(json, "ability"));
        }

    }
}
