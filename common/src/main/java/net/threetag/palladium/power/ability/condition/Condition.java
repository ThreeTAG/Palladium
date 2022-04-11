package net.threetag.palladium.power.ability.condition;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.Collections;
import java.util.List;

public abstract class Condition {

    public abstract boolean active(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder);

    public boolean needsKey() {
        return false;
    }

    public void onKeyPressed(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {

    }

    public void onKeyReleased(LivingEntity entity, AbilityEntry entry, Power power, IPowerHolder holder) {

    }

    public abstract ConditionSerializer getSerializer();

    public List<String> getDependentAbilities() {
        return Collections.emptyList();
    }
}
