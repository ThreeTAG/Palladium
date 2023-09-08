package net.threetag.palladium.data.forge;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.world.PalladiumConfiguredFeatures;
import net.threetag.palladium.world.PalladiumPlacedFeatures;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class PalladiumWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, PalladiumConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, PalladiumPlacedFeatures::bootstrap);

    public PalladiumWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Palladium.MOD_ID));
    }
}
