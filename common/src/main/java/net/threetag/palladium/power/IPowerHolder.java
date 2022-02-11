package net.threetag.palladium.power;

import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.provider.PowerProvider;

import java.util.Map;

public interface IPowerHolder {

    Power getPower();

    PowerProvider getPowerProvider();

    Map<String, AbilityEntry> getAbilities();

    void tick();

    void firstTick();

    void lastTick();

    boolean isInvalid();

}
