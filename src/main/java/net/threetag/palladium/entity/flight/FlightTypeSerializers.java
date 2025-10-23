package net.threetag.palladium.entity.flight;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class FlightTypeSerializers {

    public static final DeferredRegister<FlightTypeSerializer<?>> FLIGHT_TYPE_SERIALIZERS = DeferredRegister.create(PalladiumRegistryKeys.FLIGHT_TYPE_SERIALIZERS, Palladium.MOD_ID);

    public static final DeferredHolder<FlightTypeSerializer<?>, DefaultFlightType.Serializer> FLIGHT = FLIGHT_TYPE_SERIALIZERS.register("flight", DefaultFlightType.Serializer::new);
    public static final DeferredHolder<FlightTypeSerializer<?>, SwingingFlightType.Serializer> SWINGING = FLIGHT_TYPE_SERIALIZERS.register("swinging", SwingingFlightType.Serializer::new);

}
