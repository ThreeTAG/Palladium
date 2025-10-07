package net.threetag.palladium.entity.flight;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.phys.Vec2;
import net.threetag.palladium.Palladium;
import net.threetag.palladium.registry.PalladiumRegistryKeys;

import java.util.function.BiConsumer;

public class PalladiumFlightTypes {

    public static final ResourceKey<FlightType> LEVITATION = create("levitation");
    public static final ResourceKey<FlightType> PROPULSION = create("propulsion");
    public static final ResourceKey<FlightType> WEB_SWINGING = create("web_swinging");

    private static ResourceKey<FlightType> create(String id) {
        return ResourceKey.create(PalladiumRegistryKeys.FLIGHT_TYPES, Palladium.id(id));
    }

    public static void bootstrap(BiConsumer<ResourceKey<FlightType>, FlightType> consumer) {
        consumer.accept(LEVITATION, new DefaultFlightType(
                1F, 1F, new DefaultFlightType.AnimationSettings(Palladium.id("flight/default"), 35F, 20F, 12F))
        );

        consumer.accept(PROPULSION, new DefaultFlightType(
                1F, 3F, new DefaultFlightType.AnimationSettings(Palladium.id("flight/default"), 35F, 20F, 12F))
        );

        consumer.accept(WEB_SWINGING, new SwingingFlightType(
                120, SwingingFlightType.MaxHeightType.ABSOLUTE, new Vec2(10, 30), 40, SwingingFlightType.AnimationSettings.DEFAULT)
        );
    }

}
