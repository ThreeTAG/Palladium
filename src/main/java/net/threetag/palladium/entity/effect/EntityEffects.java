package net.threetag.palladium.entity.effect;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistries;

public class EntityEffects {

    public static final DeferredRegister<EntityEffect> EFFECTS = DeferredRegister.create(PalladiumRegistries.ENTITY_EFFECT, Palladium.MOD_ID);

    public static final DeferredHolder<EntityEffect, EnergyBeamEffect> ENERGY_BEAM = EFFECTS.register("energy_beam", EnergyBeamEffect::new);

}
