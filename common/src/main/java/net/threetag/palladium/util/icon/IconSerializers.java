package net.threetag.palladium.util.icon;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.DeferredRegistry;
import net.threetag.palladium.registry.RegistrySupplier;

public class IconSerializers {

    public static final DeferredRegistry<IconSerializer<?>> ICON_SERIALIZERS = DeferredRegistry.create(Palladium.MOD_ID, IconSerializer.RESOURCE_KEY);

    public static final RegistrySupplier<IconSerializer<ItemIcon>> ITEM = ICON_SERIALIZERS.register("item", ItemIcon.Serializer::new);
    public static final RegistrySupplier<IconSerializer<TexturedIcon>> TEXTURE = ICON_SERIALIZERS.register("texture", TexturedIcon.Serializer::new);
    public static final RegistrySupplier<IconSerializer<CompoundIcon>> COMPOUND = ICON_SERIALIZERS.register("compound", CompoundIcon.Serializer::new);
}
