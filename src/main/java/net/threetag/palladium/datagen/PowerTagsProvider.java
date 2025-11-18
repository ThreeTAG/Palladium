package net.threetag.palladium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.KeyTagProvider;
import net.threetag.palladium.power.Power;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.concurrent.CompletableFuture;

public abstract class PowerTagsProvider extends KeyTagProvider<Power> {

    public PowerTagsProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> completableFuture, String modId) {
        super(packOutput, PalladiumRegistryKeys.POWER, completableFuture, modId);
    }

}
