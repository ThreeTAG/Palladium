package net.threetag.palladium.entity.flight;

import net.threetag.palladium.Palladium;
import net.threetag.palladium.core.registry.DeferredRegister;
import net.threetag.palladium.core.registry.RegistryHolder;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

public class FlightTypeSerializers {

    public static final DeferredRegister<FlightTypeSerializer<?>> FLIGHT_TYPE_SERIALIZERS = DeferredRegister.create(Palladium.MOD_ID, PalladiumRegistryKeys.FLIGHT_TYPE_SERIALIZERS);

    public static final RegistryHolder<DefaultFlightType.Serializer> FLIGHT = FLIGHT_TYPE_SERIALIZERS.register("flight", DefaultFlightType.Serializer::new);
    public static final RegistryHolder<SwingingFlightType.Serializer> SWINGING = FLIGHT_TYPE_SERIALIZERS.register("swinging", SwingingFlightType.Serializer::new);

}
