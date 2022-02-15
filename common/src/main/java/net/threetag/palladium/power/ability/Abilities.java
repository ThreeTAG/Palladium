package net.threetag.palladium.power.ability;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.threetag.palladium.Palladium;

public class Abilities {

    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(Palladium.MOD_ID, Ability.RESOURCE_KEY);

    public static final RegistrySupplier<Ability> HEALING = ABILITIES.register("healing", HealingAbility::new);
    public static final RegistrySupplier<Ability> SLOWFALL = ABILITIES.register("slowfall", SlowfallAbility::new);

}
