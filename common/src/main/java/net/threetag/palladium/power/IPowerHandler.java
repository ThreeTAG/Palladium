package net.threetag.palladium.power;

import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public interface IPowerHandler {

    Map<ResourceLocation, IPowerHolder> getPowerHolders();

    void tick();

    void setPowerHolder(Power power, IPowerHolder holder);

    void addPower(Power power);

    void removePowerHolder(Power power);

    void removePowerHolder(ResourceLocation powerId);

    IPowerHolder getPowerHolder(Power power);

    boolean hasPower(Power power);
}
