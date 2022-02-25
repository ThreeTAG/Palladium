package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface IPowerHandler {

    Map<ResourceLocation, IPowerHolder> getPowerHolders();

    void tick();

    void setPowerHolder(ResourceLocation provider, IPowerHolder holder);

    IPowerHolder getPowerHolder(ResourceLocation provider);
}
