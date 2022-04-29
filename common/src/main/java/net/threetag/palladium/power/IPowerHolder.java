package net.threetag.palladium.power;

import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.Map;

public interface IPowerHolder {

    Power getPower();

    Map<String, AbilityEntry> getAbilities();

    void tick();

    void firstTick();

    void lastTick();

    boolean isInvalid();

    LivingEntity getEntity();

}
