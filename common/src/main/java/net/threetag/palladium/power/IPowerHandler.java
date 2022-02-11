package net.threetag.palladium.power;

import net.threetag.palladium.power.provider.IPowerProvider;

import java.util.Collection;

public interface IPowerHandler {

    Collection<IPowerHolder> getPowerHolders();

    void tick();

    IPowerHolder getPowerHolder(IPowerProvider provider);
}
