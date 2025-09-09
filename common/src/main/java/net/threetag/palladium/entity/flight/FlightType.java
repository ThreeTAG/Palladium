package net.threetag.palladium.entity.flight;

import com.mojang.serialization.Codec;
import net.threetag.palladium.registry.PalladiumRegistries;

public abstract class FlightType {

    public static final Codec<FlightType> CODEC = PalladiumRegistries.FLIGHT_TYPE_SERIALIZERS.byNameCodec().dispatch(FlightType::getSerializer, FlightTypeSerializer::codec);

    public abstract FlightTypeSerializer<?> getSerializer();

    public abstract FlightController<?> createController();

    public abstract FlightAnimationHandler<?> createAnimationHandler();

}
