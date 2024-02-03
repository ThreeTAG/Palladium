package net.threetag.palladium.power;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.power.ability.AbilityEntry;
import net.threetag.palladium.power.energybar.EnergyBar;

import java.util.Map;

public interface IPowerHolder {

    Power getPower();

    Map<String, AbilityEntry> getAbilities();

    Map<String, EnergyBar> getEnergyBars();

    void tick();

    void firstTick();

    void lastTick();

    boolean isInvalid();

    void switchValidator(IPowerValidator validator);

    LivingEntity getEntity();

    void fromNBT(CompoundTag tag);

    CompoundTag toNBT(boolean toDisk);

}
