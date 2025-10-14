package net.threetag.palladium.entity;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
import net.threetag.palladium.Palladium;

public interface PalladiumDamageTypes {

    ResourceKey<DamageType> ENERGY_BEAM = ResourceKey.create(Registries.DAMAGE_TYPE, Palladium.id("energy_beam"));

}
