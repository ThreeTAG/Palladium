package net.threetag.palladium.compat.geckolib;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.compat.geckolib.ability.GeoLayerAnimationTriggerAbility;
import net.threetag.palladium.power.ability.AbilitySerializer;
import net.threetag.palladium.registry.PalladiumRegistryKeys;
import software.bernie.geckolib.GeckoLibConstants;

public class GeckoLibCompat {

    private static final DeferredRegister<AbilitySerializer<?>> ABILITIES = DeferredRegister.create(PalladiumRegistryKeys.ABILITY_SERIALIZER, GeckoLibConstants.MODID);
    public static final DeferredHolder<AbilitySerializer<?>, AbilitySerializer<GeoLayerAnimationTriggerAbility>> TRIGGER_LAYER_ANIMATION = ABILITIES.register("trigger_layer_animation", GeoLayerAnimationTriggerAbility.Serializer::new);
    public static GeckoAbilityHandler ABILITY_HANDLER = new GeckoAbilityHandler();

    public static void init(IEventBus eventBus) {
        ABILITIES.register(eventBus);
    }

}
