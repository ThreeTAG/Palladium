package net.threetag.palladium.client.renderer.entity.layer;

import net.threetag.palladium.Palladium;

public class PackRenderLayerSerializers {

    public static final PackRenderLayerSerializer<?> DEFAULT = register("render_layer", new DefaultPackRenderLayer.Serializer());
    public static final PackRenderLayerSerializer<?> COMPOUND = register("compound", new CompoundPackRenderLayer.Serializer());
    public static final PackRenderLayerSerializer<?> LIGHTNING_SPARKS = register("lightning_sparks", new LightningSparksPackRenderLayer.Serializer());

    private static <R extends PackRenderLayer.State, T extends PackRenderLayer<R>> PackRenderLayerSerializer<T> register(String id, PackRenderLayerSerializer<T> serializer) {
        return PackRenderLayerSerializer.register(Palladium.id(id), serializer);
    }

    public static void init() {
        // nothing
    }

}
