package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.client.beam.BeamConfiguration;
import net.threetag.palladium.client.beam.PalladiumBeams;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class BeamProvider extends FabricCodecDataProvider<BeamConfiguration> {

    protected BeamProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PackOutput.Target.RESOURCE_PACK, "palladium/beams", BeamConfiguration.CODEC);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, BeamConfiguration> provider, HolderLookup.Provider lookup) {
        PalladiumBeams.bootstrap(provider);
    }

    @Override
    public String getName() {
        return "Palladium Beams";
    }
}
