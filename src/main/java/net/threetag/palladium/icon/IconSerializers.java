package net.threetag.palladium.icon;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class IconSerializers {

    public static final DeferredRegister<IconSerializer<?>> ICON_SERIALIZERS = DeferredRegister.create(PalladiumRegistryKeys.ICON_SERIALIZER, Palladium.MOD_ID);

    public static final DeferredHolder<IconSerializer<?>, CompoundIcon.Serializer> COMPOUND = ICON_SERIALIZERS.register("compound", CompoundIcon.Serializer::new);
    public static final DeferredHolder<IconSerializer<?>, ItemIcon.Serializer> ITEM = ICON_SERIALIZERS.register("item", ItemIcon.Serializer::new);
    public static final DeferredHolder<IconSerializer<?>, ItemInSlotIcon.Serializer> ITEM_IN_SLOT = ICON_SERIALIZERS.register("item_in_slot", ItemInSlotIcon.Serializer::new);
    public static final DeferredHolder<IconSerializer<?>, IngredientIcon.Serializer> INGREDIENT = ICON_SERIALIZERS.register("ingredient", IngredientIcon.Serializer::new);
    public static final DeferredHolder<IconSerializer<?>, TexturedIcon.Serializer> TEXTURE = ICON_SERIALIZERS.register("texture", TexturedIcon.Serializer::new);
    public static final DeferredHolder<IconSerializer<?>, ExperienceIcon.Serializer> EXPERIENCE = ICON_SERIALIZERS.register("experience", ExperienceIcon.Serializer::new);
}
