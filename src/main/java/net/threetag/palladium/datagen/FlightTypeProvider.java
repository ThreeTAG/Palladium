package net.threetag.palladium.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.threetag.palladium.entity.flight.FlightType;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.concurrent.CompletableFuture;

public abstract class FlightTypeProvider extends AdvancedJsonCodecProvider<FlightType> {

    public FlightTypeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId) {
        super(output, PackOutput.Target.DATA_PACK, PalladiumRegistryKeys.getPackFolder(PalladiumRegistryKeys.FLIGHT_TYPE), FlightType.CODEC, lookupProvider, modId);
    }
}
