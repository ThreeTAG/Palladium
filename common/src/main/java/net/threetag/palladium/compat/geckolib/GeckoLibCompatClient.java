package net.threetag.palladium.compat.geckolib;

import net.threetag.palladium.client.renderer.entity.layer.PackRenderLayerSerializer;
import net.threetag.palladium.compat.geckolib.layer.GeoRenderLayer;
import software.bernie.geckolib.GeckoLibConstants;

public class GeckoLibCompatClient {

    public static final PackRenderLayerSerializer<GeoRenderLayer> RENDER_LAYER = PackRenderLayerSerializer.register(GeckoLibConstants.id("render_layer"), new GeoRenderLayer.Serializer());

    public static void init() {

    }

}
