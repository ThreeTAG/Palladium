package net.threetag.palladium.condition;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.IPowerHolder;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.power.ability.AbilityConfiguration;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.util.property.PropertyManager;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public abstract class Condition {

    public abstract boolean active(LivingEntity entity, @Nullable AbilityEntry entry, @Nullable Power power, @Nullable IPowerHolder holder);

    public boolean needsKey() {
        return false;
    }

    public AbilityConfiguration.KeyType getKeyType() {
        return AbilityConfiguration.KeyType.KEY_BIND;
    }

    public boolean handlesCooldown() {
        return false;
    }

    public CooldownType getCooldownType() {
        return CooldownType.STATIC;
    }

    public void init(LivingEntity entity, AbilityEntry entry, PropertyManager manager) {

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
