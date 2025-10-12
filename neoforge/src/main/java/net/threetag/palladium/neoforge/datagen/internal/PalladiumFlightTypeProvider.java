package net.threetag.palladium.neoforge.datagen.internal;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.entity.flight.PalladiumFlightTypes;
import net.threetag.palladium.neoforge.datagen.FlightTypeProvider;

import java.util.concurrent.CompletableFuture;

public class PalladiumFlightTypeProvider extends FlightTypeProvider {

    public PalladiumFlightTypeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, Palladium.MOD_ID);
    }

    @Override
    protected void gather() {
        PalladiumFlightTypes.bootstrap((key, flightType) -> this.unconditional(key.location(), flightType));
    }
}
