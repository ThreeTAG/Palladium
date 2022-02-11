package net.threetag.palladium.power;

import net.threetag.palladium.power.provider.PowerProvider;

import java.util.Map;

public interface IPowerHandler {

    Map<PowerProvider, IPowerHolder> getPowerHolders();

    void tick();

    void setPowerHolder(PowerProvider provider, IPowerHolder holder);

    IPowerHolder getPowerHolder(PowerProvider provider);
}
