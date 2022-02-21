package net.threetag.palladium.power.ability;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.threetag.palladium.Palladium;

public class Abilities {

    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(Palladium.MOD_ID, Ability.RESOURCE_KEY);

    public static final RegistrySupplier<Ability> COMMAND = ABILITIES.register("command", CommandAbility::new);
    public static final RegistrySupplier<Ability> ATTRIBUTE_MODIFIER = ABILITIES.register("attribute_modifier", AttributeModifierAbility::new);
    public static final RegistrySupplier<Ability> HEALING = ABILITIES.register("healing", HealingAbility::new);
    public static final RegistrySupplier<Ability> SLOWFALL = ABILITIES.register("slowfall", SlowfallAbility::new);
    public static final RegistrySupplier<Ability> DAMAGE_IMMUNITY = ABILITIES.register("damage_immunity", DamageImmunityAbility::new);

}
