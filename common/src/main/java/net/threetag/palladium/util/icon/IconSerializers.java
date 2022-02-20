package net.threetag.palladium.util.icon;

import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.threetag.palladium.Palladium;

public class IconSerializers {

    public static final DeferredRegister<IconSerializer<?>> ICON_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, IconSerializer.RESOURCE_KEY);

    public static final RegistrySupplier<IconSerializer<?>> ITEM = ICON_SERIALIZERS.register("item", ItemIcon.Serializer::new);
    public static final RegistrySupplier<IconSerializer<?>> TEXTURE = ICON_SERIALIZERS.register("texture", TexturedIcon.Serializer::new);
}
