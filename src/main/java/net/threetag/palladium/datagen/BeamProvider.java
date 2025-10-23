package net.threetag.palladium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.JsonCodecProvider;
import net.threetag.palladium.client.beam.BeamConfiguration;

import java.util.concurrent.CompletableFuture;

public abstract class BeamProvider extends JsonCodecProvider<BeamConfiguration> {

    public BeamProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, PackOutput.Target.RESOURCE_PACK, "palladium/beams", BeamConfiguration.CODEC, lookupProvider, modId);
    }

}
