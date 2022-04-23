package net.threetag.palladium.power.ability;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.util.icon.TexturedIcon;

public class Abilities {

    public static final DeferredRegister<Ability> ABILITIES = DeferredRegister.create(Palladium.MOD_ID, Ability.RESOURCE_KEY);

    public static final RegistrySupplier<Ability> COMMAND = ABILITIES.register("command", CommandAbility::new);
    public static final RegistrySupplier<Ability> RENDER_LAYER = ABILITIES.register("render_layer", RenderLayerAbility::new);
    public static final RegistrySupplier<Ability> INTERPOLATED_INTEGER = ABILITIES.register("interpolated_integer", InterpolatedIntegerAbility::new);
    public static final RegistrySupplier<Ability> ATTRIBUTE_MODIFIER = ABILITIES.register("attribute_modifier", AttributeModifierAbility::new);
    public static final RegistrySupplier<Ability> HEALING = ABILITIES.register("healing", HealingAbility::new);
    public static final RegistrySupplier<Ability> SLOWFALL = ABILITIES.register("slowfall", SlowfallAbility::new);
    public static final RegistrySupplier<Ability> DAMAGE_IMMUNITY = ABILITIES.register("damage_immunity", DamageImmunityAbility::new);
    public static final RegistrySupplier<Ability> INVISIBILITY = ABILITIES.register("invisibility", () -> new Ability().withProperty(Ability.ICON, new TexturedIcon(new ResourceLocation(Palladium.MOD_ID, "textures/icon/invisibility.png"))));
    public static final RegistrySupplier<Ability> ENERGY_BLAST = ABILITIES.register("energy_blast", EnergyBlastAbility::new);
    public static final RegistrySupplier<Ability> SIZE = ABILITIES.register("size", SizeAbility::new);

    public static void init() {

    }

}
