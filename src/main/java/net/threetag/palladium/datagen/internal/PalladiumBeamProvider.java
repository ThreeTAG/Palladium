package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.client.beam.PalladiumBeams;
import net.threetag.palladium.datagen.BeamProvider;

import java.util.concurrent.CompletableFuture;

public class PalladiumBeamProvider extends BeamProvider {

    public PalladiumBeamProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        PalladiumBeams.bootstrap(this::unconditional);
    }
}
