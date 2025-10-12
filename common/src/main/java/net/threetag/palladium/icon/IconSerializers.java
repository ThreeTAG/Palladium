package net.threetag.palladium.icon;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class IconSerializers {

    public static final DeferredRegister<IconSerializer<?>> ICON_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.ICON_SERIALIZER);

    public static final RegistryHolder<CompoundIcon.Serializer> COMPOUND = ICON_SERIALIZERS.register("compound", CompoundIcon.Serializer::new);
    public static final RegistryHolder<ItemIcon.Serializer> ITEM = ICON_SERIALIZERS.register("item", ItemIcon.Serializer::new);
    public static final RegistryHolder<ItemInSlotIcon.Serializer> ITEM_IN_SLOT = ICON_SERIALIZERS.register("item_in_slot", ItemInSlotIcon.Serializer::new);
    public static final RegistryHolder<IngredientIcon.Serializer> INGREDIENT = ICON_SERIALIZERS.register("ingredient", IngredientIcon.Serializer::new);
    public static final RegistryHolder<TexturedIcon.Serializer> TEXTURE = ICON_SERIALIZERS.register("texture", TexturedIcon.Serializer::new);
    public static final RegistryHolder<ExperienceIcon.Serializer> EXPERIENCE = ICON_SERIALIZERS.register("experience", ExperienceIcon.Serializer::new);
}
