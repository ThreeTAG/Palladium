package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.threetag.palladium.client.PoseStackTransformation;
import net.threetag.palladium.customization.CustomizationCategories;
import net.threetag.palladium.customization.CustomizationCategory;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class CustomizationCategoryProvider extends FabricCodecDataProvider<CustomizationCategory> {

    protected CustomizationCategoryProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PalladiumRegistryKeys.CUSTOMIZATION_CATEGORY, CustomizationCategory.CODEC);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, CustomizationCategory> provider, HolderLookup.Provider lookup) {
        this.add(provider, CustomizationCategories.HAT, 10,
                PoseStackTransformation.create(2.5F, 0, 2.2F, 0, 15, 40, 0),
                EquipmentSlot.HEAD);
        this.add(provider, CustomizationCategories.HEAD, 20,
                PoseStackTransformation.create(2.5F, 0, 2F, 0, 15, 40, 0));
        this.add(provider, CustomizationCategories.CHEST, 30,
                PoseStackTransformation.create(2.2F, 0, 0.5F, 0, 15, 40, 0));
        this.add(provider, CustomizationCategories.ARMS, 40,
                PoseStackTransformation.create(2.2F, 0, 0.5F, 0, 15, 40, 0));
        this.add(provider, CustomizationCategories.LEGS, 50,
                PoseStackTransformation.create(2.2F, 0, -1F, 0, 15, 40, 0));
        this.add(provider, CustomizationCategories.BACK, 60,
                PoseStackTransformation.create(2.2F, 0, 0.5F, 0, 15, 130, 0));
    }

    private void add(BiConsumer<ResourceLocation, CustomizationCategory> provider, ResourceKey<CustomizationCategory> key, int sortIndex, PoseStackTransformation transformation) {
        this.add(provider, key, sortIndex, transformation, null);
    }

    private void add(BiConsumer<ResourceLocation, CustomizationCategory> provider, ResourceKey<CustomizationCategory> key, int sortIndex, PoseStackTransformation transformation, EquipmentSlot hiddenByEquipment) {
        provider.accept(key.location(), new CustomizationCategory(sortIndex, transformation, hiddenByEquipment));
    }

    @Override
    public String getName() {
        return "Customization Categories";
    }
}
