package net.threetag.palladium.entity.effect;

import net.minecraft.core.Holder;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.registry.PalladiumRegistries;

public class EntityEffects {

    public static final DeferredRegister<EntityEffect> EFFECTS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistries.ENTITY_EFFECT);

    public static final Holder<EnergyBeamEffect> ENERGY_BEAM = EFFECTS.register("energy_beam", EnergyBeamEffect::new);

}
