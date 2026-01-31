package net.threetag.palladium.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.datagen.FlightTypeProvider;
import net.threetag.palladium.entity.flight.PalladiumFlightTypes;

import java.util.concurrent.CompletableFuture;

public class PalladiumFlightTypeProvider extends FlightTypeProvider {

    public PalladiumFlightTypeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        PalladiumFlightTypes.bootstrap((key, flightType) -> this.unconditional(key.identifier(), flightType));
    }
}
