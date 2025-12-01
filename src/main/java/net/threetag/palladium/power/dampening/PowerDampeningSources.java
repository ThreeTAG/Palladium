package net.threetag.palladium.power.dampening;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class PowerDampeningSources {

    public static final DeferredRegister<PowerDampeningSource> DAMPENING_SOURCES = DeferredRegister.create(PalladiumRegistryKeys.POWER_DAMPENING_SOURCE, Palladium.MOD_ID);

    public static final DeferredHolder<PowerDampeningSource, ItemPowerDampening> ITEM = DAMPENING_SOURCES.register("item", ItemPowerDampening::new);
}
