package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public interface IPowerHandler {

    Map<ResourceLocation, IPowerHolder> getPowerHolders();

    void tick();

    IPowerHolder getPowerHolder(Power power);

    boolean hasPower(Power power);

    void removeAndAddPowers(List<Power> toRemove, List<Power> toAdd);

}
