package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.customization.CustomizationCategories;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.customization.CustomizationPreview;
import net.threetag.palladium.datagen.CustomizationCategoryProvider;

import java.util.concurrent.CompletableFuture;

public class PalladiumCustomizationCategoryProvider extends CustomizationCategoryProvider {

    public PalladiumCustomizationCategoryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        this.add(CustomizationCategories.HAT, 10,
                CustomizationPreview.create(2F, 0, 2F, 0, 15, 40, 0),
                EquipmentSlot.HEAD);
        this.add(CustomizationCategories.HEAD, 20,
                CustomizationPreview.create(2F, 0, 1.7F, 0, 15, 40, 0));
        this.add(CustomizationCategories.CHEST, 30,
                CustomizationCategory.DEFAULT_PREVIEW);
        this.add(CustomizationCategories.ARMS, 40,
                CustomizationCategory.DEFAULT_PREVIEW);
        this.add(CustomizationCategories.LEGS, 50,
                CustomizationCategory.DEFAULT_PREVIEW);
        this.add(CustomizationCategories.BACK, 60,
                CustomizationPreview.create(1, 0, 0, 0, 15, 130, 0));
    }
}
