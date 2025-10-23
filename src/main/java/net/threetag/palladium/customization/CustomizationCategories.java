package net.threetag.palladium.customization;

import net.minecraft.resources.ResourceKey;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class CustomizationCategories {

    public static final ResourceKey<CustomizationCategory> HAT = createKey("hat");
    public static final ResourceKey<CustomizationCategory> HEAD = createKey("head");
    public static final ResourceKey<CustomizationCategory> CHEST = createKey("chest");
    public static final ResourceKey<CustomizationCategory> ARMS = createKey("arms");
    public static final ResourceKey<CustomizationCategory> LEGS = createKey("legs");
    public static final ResourceKey<CustomizationCategory> BACK = createKey("back");

    private static ResourceKey<CustomizationCategory> createKey(String name) {
        return ResourceKey.create(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY, Palladium.id(name));
    }
}
