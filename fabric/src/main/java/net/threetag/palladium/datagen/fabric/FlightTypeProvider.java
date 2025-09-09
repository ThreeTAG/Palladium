package net.threetag.palladium.datagen.fabric;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.threetag.palladium.entity.flight.FlightType;
import net.threetag.palladium.entity.flight.PalladiumFlightTypes;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class FlightTypeProvider extends FabricCodecDataProvider<FlightType> {

    public FlightTypeProvider(FabricDataOutput dataOutput, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(dataOutput, registriesFuture, PalladiumRegistryKeys.FLIGHT_TYPES, FlightType.CODEC);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, FlightType> consumer, HolderLookup.Provider provider) {
        PalladiumFlightTypes.bootstrap((key, flightType) -> consumer.accept(key.location(), flightType));
    }

    @Override
    public String getName() {
        return "Palladium Flight Types";
    }
}
