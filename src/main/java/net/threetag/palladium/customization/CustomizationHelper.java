package net.threetag.palladium.customization;

import net.minecraft.core.RegistryAccess;
import net.minecraft.world.entity.LivingEntity;
import net.threetag.palladium.logic.context.DataContext;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Set;
import java.util.stream.Collectors;

public class CustomizationHelper {

    public static Set<Customization> getCustomizationsForCategory(CustomizationCategory category, RegistryAccess registryAccess) {
        var categoryKey = registryAccess.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY).getResourceKey(category).orElseThrow();
        return registryAccess.lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION).stream().filter(c -> c.getCategoryKey().equals(categoryKey)).collect(Collectors.toSet());
    }

    public static boolean hasSelectableCustomization(LivingEntity entity, CustomizationCategory category) {
        var handler = EntityCustomizationHandler.get(entity);

        if (!category.isVisible(DataContext.forEntity(entity))) {
            return false;
        }

        for (Customization customization : getCustomizationsForCategory(category, entity.registryAccess())) {
            if (handler.isUnlocked(customization)) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasSelectableCustomization(LivingEntity entity) {
        for (CustomizationCategory category : entity.registryAccess().lookupOrThrow(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY)) {
            if (hasSelectableCustomization(entity, category)) {
                return true;
            }
        }

        return false;
    }

}
