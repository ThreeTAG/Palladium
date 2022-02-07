package net.threetag.palladium.power.ability.condition;

import com.google.gson.JsonObject;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConfiguration;

public class HealthCondition extends Condition {

    private final float minHealth, maxHealth;

    public HealthCondition(float minHealth, float maxHealth) {
        this.minHealth = minHealth;
        this.maxHealth = maxHealth;
    }

    @Override
    public boolean active(LivingEntity entity, AbilityConfiguration entry, Power power, IPowerHolder holder) {
        return entity.getHealth() >= this.minHealth && entity.getHealth() <= this.maxHealth;
    }

    public static class Serializer extends ConditionSerializer {

        @Override
        public Condition fromJSON(JsonObject json) {
            return new HealthCondition(GsonHelper.getAsFloat(json, "min_health", 0), GsonHelper.getAsFloat(json, "max_health", Float.MAX_VALUE));
        }

    }

}
