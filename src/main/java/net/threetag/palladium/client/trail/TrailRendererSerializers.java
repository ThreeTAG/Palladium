package net.threetag.palladium.client.trail;

import net.threetag.palladium.Palladium;

public class TrailRendererSerializers {

    public static final TrailRendererSerializer<?> AFTER_IMAGE = register("after_image", new AfterImageTrailRenderer.Serializer());
    public static final TrailRendererSerializer<?> GRADIENT = register("gradient", new GradientTrailRenderer.Serializer());
    public static final TrailRendererSerializer<?> LIGHTNING = register("lightning", new LightningTrailRenderer.Serializer());

    private static <T extends TrailRenderer<?>> TrailRendererSerializer<T> register(String id, TrailRendererSerializer<T> serializer) {
        return TrailRendererSerializer.register(Palladium.id(id), serializer);
    }

    public static void init() {
        // nothing
    }

}
