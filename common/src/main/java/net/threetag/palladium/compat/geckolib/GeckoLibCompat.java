package net.threetag.palladium.compat.geckolib;

import net.threetag.palladium.compat.geckolib.ability.GeoLayerAnimationTriggerAbility;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class GeckoLibCompat {

    private static final DeferredRegister<AbilitySerializer<?>> ABILITIES = DeferredRegister.create("geckolib", PalladiumRegistryKeys.ABILITY_SERIALIZER);
    public static final RegistryHolder<AbilitySerializer<GeoLayerAnimationTriggerAbility>> TRIGGER_LAYER_ANIMATION = ABILITIES.register("trigger_layer_animation", GeoLayerAnimationTriggerAbility.Serializer::new);
    public static GeckoAbilityHandler ABILITY_HANDLER = new GeckoAbilityHandler();

    public static void init() {
        ABILITIES.register();
    }

}
