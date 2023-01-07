package net.threetag.palladium.util.icon;

import net.threetag.palladium.Palladium;
import net.threetag.palladiumcore.registry.DeferredRegister;
import net.threetag.palladiumcore.registry.RegistrySupplier;

@SuppressWarnings("rawtypes")
public class IconSerializers {

    public static final DeferredRegister<IconSerializer> ICON_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, IconSerializer.REGISTRY);

    public static final RegistrySupplier<IconSerializer<ItemIcon>> ITEM = ICON_SERIALIZERS.register("item", ItemIcon.Serializer::new);
    public static final RegistrySupplier<IconSerializer<TexturedIcon>> TEXTURE = ICON_SERIALIZERS.register("texture", TexturedIcon.Serializer::new);
    public static final RegistrySupplier<IconSerializer<CompoundIcon>> COMPOUND = ICON_SERIALIZERS.register("compound", CompoundIcon.Serializer::new);
    public static final RegistrySupplier<IconSerializer<ExperienceIcon>> EXPERIENCE = ICON_SERIALIZERS.register("experience", ExperienceIcon.Serializer::new);
}
