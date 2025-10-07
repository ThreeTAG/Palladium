package net.threetag.palladium.client.beam;

import net.threetag.palladium.Palladium;

public class BeamRendererSerializers {

    public static final BeamRendererSerializer<?> LASER = register("laser", new LaserBeamRenderer.Serializer());
    public static final BeamRendererSerializer<?> LIGHTNING = register("lightning", new LightningBeamRenderer.Serializer());

    private static <T extends BeamRenderer> BeamRendererSerializer<T> register(String id, BeamRendererSerializer<T> serializer) {
        return BeamRendererSerializer.register(Palladium.id(id), serializer);
    }

    public static void init() {
        // nothing
    }

}
