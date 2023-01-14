package net.threetag.palladium.condition;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.ability.AbilityUtil;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AbilityUnlockedCondition extends Condition {

    @Nullable
    private final ResourceLocation power;
    private final String abilityId;

    public AbilityUnlockedCondition(@Nullable ResourceLocation power, String abilityId) {
        this.power = power;
        this.abilityId = abilityId;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {
        AbilityEntry dependency = null;
        if(this.power != null) {
            dependency = AbilityUtil.getEntry(entity, this.power, this.abilityId);
        } else if(holder != null) {
            dependency = holder.getAbilities().get(this.abilityId);
        }
        return dependency != null && dependency.isUnlocked();
    }

    @Override
    public List<String> getDependentAbilities() {
        return List.of(this.abilityId);
    }

    @Override
    public ConditionSerializer getSerializer() {
        return ConditionSerializers.ABILITY_UNLOCKED.get();
    }

    public static class Serializer extends ConditionSerializer {

        public Serializer() {
            this.withProperty(AbilityEnabledCondition.Serializer.POWER, null);
            this.withProperty(AbilityEnabledCondition.Serializer.ABILITY, "ability_id");
        }

        @Override
        public Condition make(JsonObject json) {
            return new AbilityUnlockedCondition(this.getProperty(json, AbilityEnabledCondition.Serializer.POWER), this.getProperty(json, AbilityEnabledCondition.Serializer.ABILITY));
        }

    }
}
