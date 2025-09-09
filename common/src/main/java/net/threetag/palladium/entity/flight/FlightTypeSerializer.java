package net.threetag.palladium.entity.flight;

import com.mojang.serialization.MapCodec;
import net.threetag.palladium.documentation.Documented;

public abstract class FlightTypeSerializer<T extends FlightType> implements Documented<FlightType, T>  {

    public abstract MapCodec<T> codec();

}
