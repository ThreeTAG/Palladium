package net.threetag.palladium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.threetag.palladium.client.beam.BeamConfiguration;

import java.util.concurrent.CompletableFuture;

public abstract class BeamProvider extends AdvancedJsonCodecProvider<BeamConfiguration> {

    public BeamProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, PackOutput.Target.RESOURCE_PACK, "palladium/beams", BeamConfiguration.CODEC, lookupProvider, modId);
    }

}
