package net.threetag.palladium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.EquipmentSlot;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.CustomizationPreview;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public abstract class CustomizationCategoryProvider extends JsonCodecProvider<CustomizationCategory> {

    public CustomizationCategoryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, PackOutput.Target.DATA_PACK, PalladiumRegistryKeys.getPackFolder(PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY), CustomizationCategory.CODEC, lookupProvider, modId);
    }

    public void add(ResourceKey<CustomizationCategory> key, int sortIndex, CustomizationPreview preview) {
        this.add(key, sortIndex, preview, null);
    }

    public void add(ResourceKey<CustomizationCategory> key, int sortIndex, CustomizationPreview preview, EquipmentSlot hiddenByEquipment) {
        this.unconditional(key.identifier(), new CustomizationCategory(sortIndex, preview, false, Optional.empty(), hiddenByEquipment, null));
    }

}
