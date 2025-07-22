package net.threetag.palladium.customization;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class CustomizationSerializers {

    public static final DeferredRegister<CustomizationSerializer<?>> CUSTOMIZATION_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.CUSTOMIZATION_SERIALIZERS);

    public static final RegistryHolder<DefaultCustomization.Serializer> DEFAULT = CUSTOMIZATION_SERIALIZERS.register("customization", DefaultCustomization.Serializer::new);
    public static final RegistryHolder<BuiltinCustomization.Serializer> BUILTIN = CUSTOMIZATION_SERIALIZERS.register("builtin", BuiltinCustomization.Serializer::new);

}
