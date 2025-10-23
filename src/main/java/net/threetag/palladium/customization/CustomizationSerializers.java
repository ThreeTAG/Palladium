package net.threetag.palladium.customization;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class CustomizationSerializers {

    public static final DeferredRegister<CustomizationSerializer<?>> CUSTOMIZATION_SERIALIZERS = DeferredRegister.create(PalladiumRegistryKeys.CUSTOMIZATION_SERIALIZERS, Palladium.MOD_ID);

    public static final DeferredHolder<CustomizationSerializer<?>, DefaultCustomization.Serializer> DEFAULT = CUSTOMIZATION_SERIALIZERS.register("customization", DefaultCustomization.Serializer::new);
    public static final DeferredHolder<CustomizationSerializer<?>, BuiltinCustomization.Serializer> BUILTIN = CUSTOMIZATION_SERIALIZERS.register("builtin", BuiltinCustomization.Serializer::new);

}
