package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.power.ability.AbilityEntry;

import java.util.Map;

public interface IPowerHolder {

    Power getPower();

    ResourceLocation getPowerProvider();

    Map<String, AbilityEntry> getAbilities();

    void tick();

    void firstTick();

    void lastTick();

    boolean isInvalid();

}
